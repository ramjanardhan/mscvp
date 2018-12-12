<%@page import="com.mss.ediscv.simon.SimonBean"%>
<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@page import="com.mss.ediscv.partner.PartnerBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">

        <script language="JavaScript" src='<s:url value="/includes/js/jquery-1.9.1.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
        <script type="text/javascript">
            function doOnLoad() {
                $('#simonscreen').addClass('active');
                $('#deliveryChannelList').addClass('active');
                $('#deliveryChannelList i').addClass('text-red');
                document.getElementById('loadingAcoountSearch').style.display = "none";
                var x = document.getElementById('flag').value;
                if (x.substring(0, 1) == "1") {
                    $('#myModal').modal('show');
                }
                if (x.substring(0, 1) == "2") {
                    $('#ProcessFlowModel').modal('show');
                }

            }

        </script>
        <style>
            .suggestionList1{
                height: 250px;
                overflow-y: scroll;
                display:none;
                font-size: 11px;
                font-weight: bold;
            }
        </style>

    </head>

    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini">  

        <%
            String check = null;
            if (request.getAttribute("check") != null) {
                check = request.getAttribute("check").toString();
            }
        %>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();"class="hold-transition skin-blue sidebar-mini" ng-app="myApp" ng-controller="myCtrl">    
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
            <section class="content-header"  style="padding:5px 15px 0px 15px;">
                <h1>
                    Delivery Channel List
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Document Repository</li>
                                </ol>-->
            </section>

            <section class="content" tyle="padding-top:8px">
                <div class="box box-primary">
                    <!--                    <div class="box-header with-border"></div>-->
                    <!--<b>Basic Search</b>-->
                    <div class="box-tools pull-right">
                    </div>


                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();%>
                                <s:form action="%{formAction}" method="post" name="partnerForm" id="partnerForm" theme="simple">
                                    <s:hidden name="formAction" id="formAction" value="%{formAction}"/>
                                    <div id="resMsg" style="color: red"></div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">

                                                <div class="row" style="margin-bottom: 10px;">
                                                    <div class="col-sm-2"> <label>Partner Name</label>
                                                        <s:textfield name="partnername"  id="partnername" cssClass="form-control pull-left" value="%{partnername}" tabindex="1"/> 
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <strong><input style="margin-top:23px;" type="button" value="Select" class="btn btn-primary col-sm-12"  data-toggle="modal" data-target="#myModal" tabindex="2"></strong>
                                                    </div>

                                                    <div class="col-sm-2">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Active','Inactive'}" name="status" id="status" value="%{status}" tabindex="3" /> 
                                                    </div>





                                                    <div  class="col-sm-2" style="display: none">
                                                        <label>Routing Table Name</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="4"/> 
                                                    </div>

                                                    <div  class="col-sm-2">
                                                        <label>Process Flow Name</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="4"/> 
                                                    </div>

                                                    <div class="col-sm-2">
                                                        <strong><input style="margin-top:23px;" type="button" value="Select" class="btn btn-primary col-sm-12"  data-toggle="modal" data-target="#ProcessFlowModel" tabindex="5"></strong>
                                                    </div>

                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Archive Flag</label>  
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Yes','No'}" name="status" id="status" value="%{status}" tabindex="5" /> 
                                                    </div>
                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Archive Directory</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="6"/> 
                                                    </div>
                                                </div>


                                                <div id="internalidDetails" style="display:none;">
                                                    <b>Sender Id  : </b><s:textfield name="internalid"  id="internalid" value="%{' '}"  style="border:0;" tabindex="7"/> 
                                                </div>
                                                <div id="partneridDetails" style="display:none;">
                                                    <b>Receiver Id  : </b><s:textfield name="partnerid"  id="partnerid" value="%{' '}"  style="border:0;" tabindex="8"/> 
                                                </div>                   
                                                <div id="applicationidDetails" style="display:none;margin-bottom: 5px;">
                                                    <b>Application Id  : </b><s:textfield name="applicationid"  id="applicationid" value="%{' '}"  style="border:0;" tabindex="9"/> 
                                                </div>



                                                <div class="row" style="margin-bottom: 10px;">

                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Business Process Name</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="10"/> 
                                                    </div> 
                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Output File Name</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="11"/> 
                                                    </div>
                                                    <div  class="col-sm-2"style="display: none">
                                                        <div class="form-group ">
                                                            <label for="inputdefault">Translation Map Name</label>

                                                            <!-- <input class="form-control" id="map" type="text" ng-keyup="getEmployeesList()" ng-model="map.searchKey" onchange= "fieldLengthValidator(this)"> -->
                                                            <input class="form-control" id="map" type="text" ng-keyup="getMapList()" ng-model="map.searchKey" tabindex="12">
                                                        </div>
                                                        <div id="load1"  style=" color: red;display:none;font: bold;">Loading...
                                                        </div>
                                                        <div id="suggestionList1" class="suggestionList1" >
                                                            <div ng-repeat="map in mapList" ng-model="consultant" ng-click="selectMap(map)">
                                                                <a href="" class="suggestionList">{{map.name}} </a>
                                                            </div>
                                                        </div>

                                                    </div>

                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Output Format</label>  
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'EDI','APP','XML','SAP'}" name="status" id="status" value="%{status}" tabindex="13" /> 
                                                    </div>


                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Document Extract Map</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="14"/> 
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <div class="form-group">
                                                            <label for="inputdefault">Producer Mailbox</label>
                                                            <!--<input class="form-control" id="sourceMailBox" type="text" onchange= "fieldLengthValidator(this)"> -->
                                                            <input class="form-control" id="sourceMailBox" type="text" ng-keyup="getSourceMailBoxList()" ng-model="source.searchKey" tabindex="15">
                                                        </div>
                                                        <div id="load2"  style=" color: red;display:none;font: bold;">Loading...
                                                        </div>
                                                        <div id="suggestionList2" class="suggestionList1" >
                                                            <div ng-repeat="mailBox in mailBoxList" ng-model="consultant" ng-click="selectSourceMailBox(mailBox)">
                                                                <a href="" class="suggestionList">{{mailBox.name}} </a>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>



                                                <div class="row" style="margin-bottom: 10px;">

                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Partner Sender ID</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="16"/> 
                                                    </div>
                                                    <div  class="col-sm-2"style="display: none">
                                                        <label>Partner Receiver ID</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="17"/> 
                                                    </div>


                                                    <div class="col-sm-2"style="display: none">
                                                        <label>Partner Application Id</label>
                                                        <s:textfield name="applicationid"  id="applicationid" cssClass="form-control pull-left" value="%{applicationid}" tabindex="18"/> 
                                                    </div>
                                                    <div class="col-sm-2"style="display: none">
                                                        <label>System Type</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="19" /> 
                                                    </div>
                                                    <div class="col-sm-2 pull-left " style="width: 13.66666667%;margin-top: 30px; display: none"   >
                                                        <input type="checkbox" name="wildcards" tabindex="20"  value="wildcards" checked><label>Use WildCards</label> 
                                                    </div> 
                                                    <div class="col-sm-2  pull-left" style="width: 13.66666667%;margin-top: 30px; display: none">
                                                        <input type="checkbox" name="ignorecase" tabindex="21"  value="ignorecase" checked><label> Ignore Case</label>
                                                    </div> 
                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>

                                                </div>


                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 10px;" >

                                            <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="22"></strong></div>


                                            <div class="col-sm-2 pull-left"><s:submit value="Search"  id="hideshow"   cssClass="btn btn-primary col-sm-12" tabindex="23"/></div>

                                            </td>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        </s:form>

                                    </div>

                                    <span id="span1">
                                    </span>
                                    <div class="row">

                                    </div>
                                </div>
                            </div>
                        </div></div>

                    <!-- Control Sidebar -->
                    <!-- /.control-sidebar -->
                    <!-- Add the sidebar's background. This div must be placed
                         immediately after the control sidebar -->
                    <!-- ./wrapper -->
                </div>
            </section>
            <div id="gridDiv">     
                <%--<s:if test="">--%>
                <%--- GRid start --%>
                <section class="content">
                    <div class="row" id="hideshow1" style="display: none">
                        <div class="col-xs-12">
                            <div class="box">
<!--                                <div class="box-header">
                                    <h3 class="box-title">Table</h3>
                                </div> /.box-header -->
                                <div class="box-body" style="overflow-x:auto;">
                                    <%!String cssValue = "whiteStripe";
                                        int resultsetTotal;%>
                                    <div>                 
                                        <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0" >
                                            <tr>
                                                <td style="background-color: white;">
                                                    <div>
                                                        <table id="results"  class="table table-bordered table-hover">

                                                            <%-- <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOC_LIST);
                                                                if (list.size() != 0) {
                                                                    DocRepositoryBean docRepositoryBean;
                                                            %>--%>
                                                            <thead><tr>
                                                                    <th >Action</th>
                                                                    <th >Partner&nbsp;Name</th> 
                                                                    <th >Routing&nbsp;Table&nbsp;Name</th>
                                                                    <th >Sequence</th>
                                                                    <th >Business&nbsp;Process&nbsp;Name</th>
                                                                    <th >Transaction&nbsp;Map&nbsp;Name</th>
                                                                    <th >Document&nbsp;Extract&nbsp;Map&nbsp;Name</th>
                                                                    <th >Archive&nbsp;Flag</th>
                                                                    <th >Archive&nbsp;Directory</th>
                                                                    <th >Output&nbsp;File&nbsp;Name</th>
                                                                    <th >Output&nbsp;Format</th>
                                                                    <th >Producer&nbsp;Mailbox</th>
                                                                    <th >System&nbsp;Type</th>

                                                            <tbody>
                                                                <%--  <%
    //                                                                        String corrattribute;
    //                                                                        String corrattribute1;
    //                                                                        String corrattribute2;
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        docRepositoryBean = (DocRepositoryBean) list.get(i);
    //                                                                            corrattribute = docRepositoryBean.getCorrattribute();
    //                                                                            corrattribute1 = docRepositoryBean.getCorrattribute1();
    //                                                                            corrattribute2 = docRepositoryBean.getCorrattribute2();

                                                                            //if (corrattribute != "-1" && !"-1".equalsIgnoreCase(corrattribute) && i == 0) {
                                                                    %>--%>
                                                                <%--<td><%=corrattribute%> </td> 
                                                                <%}
                                                                    if (corrattribute1 != "-1" && !"-1".equalsIgnoreCase(corrattribute1) && i == 0) {%>
                                                                <td ><%=corrattribute1%> </td> 
                                                                <%}
                                                                    if (corrattribute2 != "-1" && !"-1".equalsIgnoreCase(corrattribute2) && i == 0) {%>
                                                                <td ><%=corrattribute2%> </td> 
                                                                <%
                                                                    }
                                                                %> --%>

                                                                <tr>
                                                                    <td>efg
                                                                        <%--  <%  if (docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                      && !"".equals(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                  out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                              } else {
                                                                                  out.println("-");
                                                                              }
                                                                              //out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                          %>--%>
                                                                    </td> 
                                                                    <td>Test</td>
                                                                    <td>Test</td>
                                                                    <td>Test</td>
                                                                    <td>Test
                                                                        <%-- <%                                                                            if (docRepositoryBean.getFile_type() != null && !"".equals(docRepositoryBean.getFile_type())) {
                                                                                 out.println(docRepositoryBean.getFile_type());
                                                                             } else {
                                                                                 out.println("-");
                                                                             }
                                                                             //out.println(docRepositoryBean.getFile_type());
                                                                          %>--%>
                                                                    </td>
                                                                    <%-- <td><a href="javascript:getDetails('<%=docRepositoryBean.getFile_id()%>','<%=docRepositoryBean.getPoNumber()%>','<%=docRepositoryBean.getId()%>');">--%>
                                                                    <%--<%
                                                                        if (docRepositoryBean.getFile_id() != null && !"".equals(docRepositoryBean.getFile_id())) {
                                                                            out.println(docRepositoryBean.getFile_id());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                       // out.println(docRepositoryBean.getFile_id());
                                                                    %>--%>


                                                                    <td>Test
                                                                        <%--   <%
                                                                               if (docRepositoryBean.getPname() != null && !"".equals(docRepositoryBean.getPname())) {
                                                                                   out.println(docRepositoryBean.getPname());
                                                                               } else {
                                                                                   out.println("-");
                                                                               }
                                                                           %>--%>
                                                                    </td>
                                                                    <td>Test
                                                                        <%-- <%                                                                            //out.println(docRepositoryBean.getTransaction_type());
                                                                             if (docRepositoryBean.getTransaction_type() != null && !"".equals(docRepositoryBean.getTransaction_type())) {
                                                                                 out.println(docRepositoryBean.getTransaction_type());
                                                                             } else {
                                                                                 out.println("-");
                                                                             }
                                                                         %>--%>
                                                                    </td>
                                                                    <td>Test
                                                                        <%--  <%
                                                                              if (docRepositoryBean.getDirection() != null && !"".equals(docRepositoryBean.getDirection())) {
                                                                                  out.println(docRepositoryBean.getDirection().toUpperCase());
                                                                              } else {
                                                                                  out.println("-");
                                                                              }
                                                                          %>--%>
                                                                    </td>  
                                                                    <td>Test
                                                                        <%--  <%
                                                                              if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                  out.println("<font color='red'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                              } else if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                  out.println("<font color='green'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                              } else {
                                                                                  out.println("<font color='orange'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                              }
                                                                          %>--%>
                                                                    </td>
                                                                    <td>Test
                                                                        <%-- <%
                                                                             if (docRepositoryBean.getReProcessStatus() != null && !"".equals(docRepositoryBean.getReProcessStatus())) {
                                                                                 out.println(docRepositoryBean.getReProcessStatus());
                                                                             } else {
                                                                                 out.println("-");
                                                                             }
                                                                         %>--%>
                                                                    </td>
                                                                    <td>Test
                                                                        <%-- <%
                                                                             if (docRepositoryBean.getAckStatus() != null && !"".equals(docRepositoryBean.getAckStatus())) {
                                                                                 out.println(docRepositoryBean.getAckStatus().toUpperCase());
                                                                             } else {
                                                                                 out.println("-");
                                                                             }
                                                                             //out.println(docRepositoryBean.getAckStatus());
                                                                         %>--%>
                                                                    </td>
                                                                    <%--<%if (docRepositoryBean.getCorrvalue() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue())) {%>
                                                                    <td ><%=docRepositoryBean.getCorrvalue()%> </td> 
                                                                    <%}
                                                                        if (docRepositoryBean.getCorrvalue1() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue1())) {%>
                                                                    <td ><%=docRepositoryBean.getCorrvalue1()%> </td> 
                                                                    <%}
                                                                        if (docRepositoryBean.getCorrvalue2() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue2())) {%>
                                                                    <td ><%=docRepositoryBean.getCorrvalue2()%> </td> 
                                                                    <%}
                                                                    %> --%>



                                                                    <%--  <%
                                                                          }
                                                                      } else {
                                                                      %>--%>
                                                                    <td>Test
                                                                        <%--  <%
                                                                                  out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                              }
                                                                          %>--%>
                                                                    </td>
                                                                    <td>Test</td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <%--  <%
                                          if (list.size() != 0) {
                                      %>--%>
                                    <!--<tr>
                                        <td align="right" colspan="28" style="background-color: white;">
                                    <!--                                                        <div align="right" id="pageNavPosition">hello</div>-->
                                    <!--</td>-->
                                    <!--</tr>-->
                                    <%--  <% }%> --%>      


                                    <%-- Process butttons  start --%>
                                    <%-- <%
                                         if (list.size() != 0) {
                                     %>--%>
                                    <!--                                        <table align="right">
                                                                                <tr>
                                                                                    <td style="background-color: white;">
                                                                                        <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('document', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                                                    </td>
                                                                                </tr>
                                                                            </table> -->
                                    <%-- <%}%>--%>
                                </div>
                            </div></div>
                    </div></section>
                <!-- /.col -->
                <!-- /.box -->
                <!-- /.box -->
                <!-- /.row -->
                <%-- process buttons end--%>
                <%-- Grid End --%>
                <div id="hide-menu1" class="hide-menu message ">

                    <div class="row col-sm-12">

                        <br>
                        <div class="col-sm-6"> <label class="labelw"> File ID</label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="ManFileId" name="ManFileId"  readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">  Purchase Order</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManPurchaseOrder" name="ManPurchaseOrder" readonly="true"/>
                        </div>
                    </div>
                    <div class="row col-sm-12" id="prikeytypeandvalue" style="display:none"> 
                        <div class="col-sm-6"><label class="labelw">Doc Type</label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="Manpri_key_type" name="Manpri_key_type" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">Doc Number</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="Manpri_key_value" name="Manpri_key_value" readonly="true"/>
                        </div>
                    </div>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw"> Doc Format</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManDocumentType" name="ManDocumentType" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Transaction Type</label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="ManTransactionType" name="ManTransactionType" readonly="true"/>
                        </div>
                    </div>
                    <br>
                    <div id="senderinfo">
                        <div class="row col-sm-12">
                            <div class="col-sm-6"> <h4>Sender Info:</h4></div>
                            <div class="col-sm-6"></div>
                            <div class="col-sm-6"></div>

                        </div>
                        <br>
                        <div class="row col-sm-12">
                            <div class="col-sm-6"> <label class="labelw">  Sender Id</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSenderId" name="ManSenderId" readonly="true"/>
                            </div>
                            <div class="col-sm-6"><label class="labelw">  Sender Name</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSenderName" name="ManSenderName" readonly="true"/>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div id="receiverinfo">
                        <div class="row col-sm-12">
                            <div class="col-sm-6"> <h4>Receiver Info:</h4></div>
                            <div class="col-sm-6"></div>
                            <div class="col-sm-6"></div>
                        </div>

                        <br>
                        <div class="row col-sm-12 clear">
                            <div class="col-sm-6"><label class="labelw">  Receiver Id</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManReceiverId" name="ManReceiverId" readonly="true"/>
                            </div>
                            <div class="col-sm-6"><label class="labelw">  Receiver Name</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManReceiverName" name="ManReceiverName" readonly="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="row col-sm-12 clear" >
                        <br>
                        <div class="col-sm-6"> <label class="labelw">   ISA</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManISA" name="ManISA" readonly="true"/>
                        </div>
                        <div class="col-sm-6"><label class="labelw">  GS</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManGs" name="ManGs" readonly="true"/>
                        </div>
                    </div>


                    <br/>

                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> ST</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSt" name="ManSt" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> ISA Date</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManIsADate" name="ManIsADate" readonly="true"/>
                        </div>

                        <div class="col-sm-6"> <label class="labelw"> ISA Time</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManIsATime" name="ManIsATime" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">  Status </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManStatus" name="ManStatus" readonly="true"/>
                        </div>
                    </div>

                    <div class="row col-sm-12" style="margin-top:10px;">
                        <div class="col-sm-6"> <label class="labelw"> Pre Translation</label></div>
                        <div class="col-sm-6"><div id="ManPreTranslation"></div>
                        </div></div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"><label class="labelw"> Post Translation</label></div>
                        <div class="col-sm-6"> <div id="ManPostTranslation"></div></div>
                    </div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> 997 ACKFile</label></div>
                        <div class="col-sm-6"> <div id="ManAckFileId"></div></div>   
                    </div>


                    <div class="row col-sm-12" id="errorDiv" style="display: none">
                        <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Message </label></div>
                        <div class="col-sm-6" id="InvErrormessage" style="color: red"></div>
                    </div>
                    <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                        <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                        <div class="col-sm-6"><div id="ErrReport"></div></div>
                    </div>
                    <%--<div class="row col-sm-12 clear" style="visibility: hidden">
                        <div class="col-sm-6"> <label class="labelw"> SAP_USER </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="SAP_USER" name="ManStatus" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">  IDOC_NUMBER </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_NUMBER" name="ManStatus" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">  PO_NUMBER </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_NUMBER" name="ManStatus" readonly="true"/>
                        </div>
                        <div class="col-sm-6"><label class="labelw">  PO_DATE </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_DATE" name="ManStatus" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> IDOC_STATUS_CODE </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_STATUS_CODE" name="ManStatus" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">  IDOC_STATUS_DESCRIPTION </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_STATUS_DESCRIPTION" name="ManStatus" readonly="true"/>
                        </div>
                    </div> --%>
                    <div class="row col-sm-12" id="ManNullValues" style="display: none">
                        <div class="col-sm-6"> <label class="labelw"> display null values;</label></div></div>
                    <div id="noresult"></div>
                    <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>
                </div>


                <%--  </s:if> --%>
            </div>

        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <!-- jQuery 2.1.4 -->

        <!-- jQuery UI 1.11.4 -->


        <!-- daterangepicker -->


        <!-- AdminLTE App -->
        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>


        <script src='<s:url value="../includes/js/processFlow/processFlow.js?version=11"/>'></script>
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
            $("#hideshow").click(function () {
                $("#hideshow1").show();
                return false;
            });
            function partnerData(pName, internalId, partnerId, applicationId) {
                $('#myModal').modal('hide');
                document.getElementById("internalidDetails").style.display = "block";
                document.getElementById("partneridDetails").style.display = "block";
                document.getElementById("applicationidDetails").style.display = "block";
                // alert("internalId"+internalId);
                if (pName == "null") {
                    pName = "--";
                }
                if (internalId == "null" || internalId == "") {
                    internalId = "    --";
                }
                if (partnerId == "null" || partnerId == "") {
                    partnerId = "    --";
                }
                if (applicationId == "null" || applicationId == "") {
                    applicationId = "    --";
                }
                document.getElementById("partnername").value = "  " + pName;
                document.getElementById("internalid").value = "  " + internalId;
                document.getElementById("partnerid").value = "  " + partnerId;
                document.getElementById("applicationid").value = "  " + applicationId;
                //alert("hiii manikanta");
            }


        </script>

        <!--Overlay for partner list--> 

        <div class="modal fade" id="myModal" role="dialog">

            <div class="modal-dialog" style="width:1000px;">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h3 class="modal-title" style="color:white;text-align:center;">Partner Search</h3>
                    </div><br>
                    <div class="modal-body">

                        <section class="content">
                            <div class="row">
                                <div class="col-sm-12"> 

                                    <%
                                        if (request.getSession(false).getAttribute("responseString") != null) {
                                            String reqponseString = request.getSession(false).getAttribute("responseString").toString();
                                            request.getSession(false).removeAttribute("responseString");
                                            out.println(reqponseString);
                                        }
                                    %>
                                    <div  style="alignment-adjust:central;" >

                                        <s:form action="../simon/deliveryChannelList.action?flag=1" method="post" name="partnerSearchForm" id="partnerSearchForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12" style="margin-top: -25px;margin-bottom: 10px;">

                                                        <div class="col-xs-2"> <label for="partnerName ">Partner Name </label>

                                                            <s:textfield cssClass="form-control" name="partnerName" id="partnerName"  value="%{partnerName}"/>
                                                        </div>



                                                        <div  class="col-xs-2">
                                                            <label for="status">Status</label> 
                                                            <s:select list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}" name="status" id="status" value="%{status}"  cssClass="form-control"/>
                                                        </div>

                                                        <div  class="col-xs-2">
                                                            <label for="internalIdentifier">Internal Identifier </label>  
                                                            <s:textfield cssClass="form-control" name="internalIdentifier" id="internalIdentifier"  value="%{internalIdentifier}" onchange="fieldLengthValidator(this);"/>
                                                        </div>

                                                        <div  class="col-xs-2">
                                                            <label for="partnerIdentifier">Partner Identifier </label>  
                                                            <s:textfield cssClass="form-control" name="partnerIdentifier" id="partnerIdentifier"  value="%{partnerIdentifier}" onchange="fieldLengthValidator(this);makeUpperCase(this);"/>
                                                        </div>

                                                        <div class="col-xs-2">
                                                            <label for="applicationId">Application ID</label>
                                                            <s:textfield cssClass="form-control" name="applicationId" id="applicationId" value="%{applicationId}"  onchange="fieldLengthValidator(this);"/>
                                                        </div>
                                                        <div class="col-xs-2">
                                                            <label for="corrvalue">Country Code</label>
                                                            <s:textfield cssClass="form-control" name="countryCode" id="countryCode" value="%{countryCode}"  onchange="fieldLengthValidator(this);"/>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div> 
                                                    <s:submit value="Search" cssClass="btn btn-primary pull-right col-xs-1" style="margin-bottom:-20px; margin-right: 13px;"/>
                                                    <s:hidden name="flag" id="flag" value="%{flag}"/>
                                                </div>
                                                <br>
                                                <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                            </s:form>


                                        </div>
                                    </div>


                                </div>

                            </div>
                        </section>

                        <div id="gridDiv">
                            <s:if test="#session.deliveryChannelPartnerList != null"> 
                                <%--- GRid start --%>
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box">
                                                <div class="box-header">
                                                    <h3 class="box-title">Table</h3>
                                                </div>
                                                <div class="box-body">

                                                    <div style="overflow-x:auto;">                 

                                                        <table align="left" width="100%"
                                                               border="0" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="background-color: white;">

                                                                    <table align="left" id="results" width="100%"
                                                                           border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                                        <%
                                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DELIVERY_PARTNER_LIST);

                                                                            if (list.size() != 0) {
                                                                                SimonBean partnerBean;
                                                                        %>
                                                                        <thead>  <tr>
                                                                                <th >Action</th>
                                                                                <th >PartnerName </th>
                                                                                <th>InternalIdentifier</th>
                                                                                <th>PartnerIdentifier</th>
                                                                                <th>ApplicationId </th>
                                                                                <th>CountryCode </th>
                                                                                <th>Status </th>
                                                                                <th>CreatedDate </th> </tr></thead><tbody>
                                                                                    <%--     <td>Changed Date</td>
                                                                                         <td>ChangesUser </td> --%>



                                                                            <tr >

                                                                                <%
                                                                                    for (int i = 0; i < list.size(); i++) {
                                                                                        partnerBean = (SimonBean) list.get(i);

                                                                                        if (i % 2 == 0) {
                                                                                            cssValue = "whiteStripe";
                                                                                        } else {
                                                                                            cssValue = "grayEditSelection";
                                                                                        }
                                                                                %>
                                                                                <td style="text-align: left"><%-- <a href="#"> --%>
                                                                                    <%
                                                                                        String id = partnerBean.getPartnerIdentifier();
                                                                                    %>
                                                                                    <input type="button" value="Select" class="btn btn-primary col-sm-12" tabindex="19" onclick="partnerData('<%=partnerBean.getPartnerName()%>', '<%=partnerBean.getInternalIdentifier()%>', '<%=partnerBean.getPartnerIdentifier()%>', '<%=partnerBean.getApplicationId()%>');"></strong>


                                                                                    <%--  </a> --%>
                                                                                </td>

                                                                                <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getPartnerName() != null && !"".equals(partnerBean.getPartnerName())) {%>
                                                                                    <a href="#" onclick="getPartnerDetails('<%=partnerBean.getPartnerIdentifier()%>')" onmouseover="Tip('Click here to view Detail Info.')" onmouseout="UnTip()"> 
                                                                                        <%
                                                                                            out.println(partnerBean.getPartnerName());
                                                                                        %></a>
                                                                                        <%
                                                                                            } else {
                                                                                                out.println("-");
                                                                                            }
                                                                                        %>
                                                                                </td>

                                                                                <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getInternalIdentifier() != null && !"".equals(partnerBean.getInternalIdentifier())) {
                                                                                            out.println(partnerBean.getInternalIdentifier());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                </td>
                                                                                <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getPartnerIdentifier() != null && !"".equals(partnerBean.getPartnerIdentifier())) {
                                                                                            out.println(partnerBean.getPartnerIdentifier());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                </td>
                                                                                <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getApplicationId() != null && !"".equals(partnerBean.getApplicationId())) {
                                                                                            out.println(partnerBean.getApplicationId());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                </td>
                                                                                <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getCountryCode() != null && !"".equals(partnerBean.getCountryCode())) {
                                                                                            out.println(partnerBean.getCountryCode());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                </td> <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getStatus() != null && !"".equals(partnerBean.getStatus())) {
                                                                                            out.println(partnerBean.getStatus());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                </td> <td style="text-align: left">
                                                                                    <%
                                                                                        if (partnerBean.getCreatedDate() != null && !"".equals(partnerBean.getCreatedDate())) {
                                                                                            out.println(partnerBean.getCreatedDate());
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
                                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                                        }

                                                                                    %>
                                                                                </td>
                                                                            </tr>
                                                                    </table>

                                                                </td>
                                                            </tr>
                                                            <tr>

                                                            </tr>   </tbody>        
                                                        </table>
                                                    </div>
                                                    <%-- Process butttons  start --%>
                                                    <table align="left" 
                                                           width="100%" border="0" cellpadding="0"
                                                           cellspacing="0">


                                                    </table>
                                                    <%-- process buttons end--%>
                                                    <%-- Grid End --%>

                                                </div></section>



                                            </s:if> 
                                        </div>

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="sessionClose();">Close</button>
                                    </div>
                                </div>
                        </div>
                    </div>
                    <!--Overlay for Process Flow-->
                    <div class="modal fade" id="ProcessFlowModel" role="dialog">
                        <div class="modal-dialog" style="width:1000px;">
                            <div class="modal-content">
                                <div class="modal-header" style="background-color: #00aae7;">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h3 class="modal-title" style="color:white;text-align:center;">Process Flow</h3>
                                </div>
                                <div class="modal-body">
                                   <section class="content">

                                        <div class="box box-primary">
                                            <div class="box-body">
                                                <div id="text">

                                                    <div style="alignment-adjust:central;" >

                                                        <s:form action="../simon/deliveryChannelList.action?flag=2" method="post" name="arcHisForm" id="arcHisForm" theme="simple">
                                                            <div class="form-group">
                                                                <div class="row">

                                                                    <div class="col-sm-2">
                                                                        <label>Tp Name</label>
                                                                        <s:textfield cssClass="form-control"  name="docTpName" id="docTpName" value="%{docTpName}"/>
                                                                    </div>
                                                                    <div class="col-sm-2">
                                                                        <label>Tp Id</label>
                                                                        <s:textfield cssClass="form-control"  name="docTpId" id="docTpId" value="%{docTpId}"/>
                                                                    </div>
                                                                    <div class="col-sm-2">
                                                                        <label>Tp Sender Id</label>
                                                                        <s:textfield cssClass="form-control"  name="docTpSenderId" id="docTpSenderId" value="%{docTpSenderId}"/>
                                                                    </div>

                                                                
                                                                    <div class="col-sm-2">
                                                                        <label>TP Receiver Id</label>
                                                                        <s:textfield cssClass="form-control"  name="docTpReceiverId" id="docTpReceiverId" value="%{docTpReceiverId}"/>
                                                                    </div>
                                                                    <div class="col-sm-2">
                                                                        <label>Transaction Type</label>
                                                                        <s:textfield cssClass="form-control"  name="docTransactionType" id="docTransactionType" value="%{docTransactionType}"/>
                                                                    </div>
                                                                    <div class="col-sm-2">
                                                                        <label for="status">Status</label>
                                                                        <s:select headerKey="" headerValue="All" cssClass="form-control" list="#@java.util.LinkedHashMap@{'Y':'Active','N':'InActive'}" name="docStatus" id="docStatus" value="%{docStatus}"/> 
                                                                    </div>
                                                                    </div>
                                                                <br>
                                                                <div class ="row">
                                                                    <div class="col-sm-2"><s:submit value="Search"  cssClass="btn btn-primary col-sm-12"/></div>
                                                                    <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>
                                                                    <div class="col-sm-2"><strong><input type="button" value="Add"   class="btn btn-primary col-sm-12" onclick="window.location.href = './addFlow.action'"/></strong></div>
                                                                </div>


                                                            </s:form>

                                                        </div>
                                                    </div>
                                                </div>
                                            </div></div>
                                    </section>
                                    <div id="gridDiv">  
                <s:if test="#session.deliveryChannelProcessFlowGrid!=null"> 
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <div class="box-header">
                                        <h3 class="box-title">Result</h3>
                                    </div><!-- /.box-header -->
                                    <div class="box-body">
                                        <div style="overflow-x:auto;">                 
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div > 

                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List listProcess = (java.util.List) session.getAttribute(AppConstants.SES_DELIVERY_PROCESS_FLOW_GRID);
                                                                    if (listProcess.size() != 0) {
                                                                %>
                                                                <thead> <tr>
                                                                        <th>PROCESS_NAME</th>
                                                                        <th>DIRECTION</th>
                                                                        <th>TP_NAME</th>
                                                                        <th>TP_ID</th>
                                                                        <th>TP_SENDER_ID</th>
                                                                        <th>TP_RECEIVER_ID</th>
                                                                        <th>TRANSACTION_TYPE</th>
                                                                        <th>IS_ACTIVE</th>
                                                                        <th>LOOKUP_ALIAS</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        for (int i = 0; i < listProcess.size(); i++) {
                                                                            Map data = (Map) listProcess.get(i);
                                                                    %>

                                                                    <tr><td><a class="code" href="../utilities/addFlow.action?processId=<%=data.get("PROCESS_ID")%>&docTpName=<s:property value='docTpName'/>&docTpId=<s:property value='docTpId'/>&docTpSenderId=<s:property value='docTpSenderId'/>&docTpReceiverId=<s:property value='docTpReceiverId'/>&docTransactionType=<s:property value='docTransactionType'/>&docStatus=<s:property value='docStatus'/>"><%=data.get("PROCESS_NAME")%></a></td>

                                                                        <td><%=data.get("DIRECTION")%></td>
                                                                        <td><%=data.get("TP_NAME")%></td>
                                                                        <td><%=data.get("TP_ID")%></td>
                                                                        <td><%=data.get("TP_SENDER_ID")%></td>
                                                                        <td><%=data.get("TP_RECEIVER_ID")%></td>
                                                                        <td><%=data.get("TRANSACTION_TYPE")%></td>
                                                                        <td><%=data.get("IS_ACTIVE")%></td>
                                                                    <td><%=data.get("LOOKUP_ALIAS")%></td>
                                                                    </tr>
                                                                    <%}%>

                                                                </tbody><%
                                                                } else {
                                                                %>
                                                                <tr><td>
                                                                        <%
                                                                                out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                            }

                                                                        %>
                                                                    </td>
                                                                </tr>
                                                            </table> 

                                                        </div></td></tr></table>
                                        </div>
                                    </div>
                                </div>
                            </div></section>

                </s:if>   

            </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    </body>
                    </html>



