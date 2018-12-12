<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.purge.PurgeHistoryBean"%>
<%@page import="com.mss.ediscv.logisticsloadtendering.LogisticsLoadBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
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


        <%--   <script language="JavaScript"
        src='<s:url value="/includes/js/generalValidations.js"/>'></script>  --%>
        <script>
            function doOnLoad()
            {
                $("#utilities").addClass("active");
                $("#processFlowMenu").addClass("active");
                $("#processFlowMenu i").addClass("text-red");


                // document.getElementById('loadingAcoountSearch').style.display = "none";
            }


        </script>

        <style>

            .demo {
                height: 2000px;


                width:2000px;

            }

            .flowchart-operator-title{
                font-size: 12px;
                padding: 2px;
                text-align: center;
            }
            .flowchart-operator-inputs-outputs{
                display: table;
                width: 100%;
                margin-top: 0px  !important ; 
                margin-bottom: 0px  !important;
            }

            #closePropertiesDiv,.btn-success{
                cursor: pointer; cursor: hand;
            }
            li { cursor: pointer; cursor: hand; 

            }

            li.active>a:hover {
                color: white !important;
                background-color: black !important;
                border-color: #01549b;
            }
            .suggestionList1{
                height: 250px;
                overflow-y: scroll;
                display:none;
                font-size: 11px;
                font-weight: bold;
            }
             .sourceNode{
                background-color: #8941f4 !important;
                color: white !important;
            }
            .mapNode{
               background-color: #4286f4!important;
                 color: white !important;
            }
            .targetNode{
                background-color: #41f46a !important;
                 color: white !important;
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini" ng-app="myApp" ng-controller="myCtrl">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper" style="min-height: 543px;">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <section class="content-header">
                 <span style="font-size: 25px;">
                    Process Flow
                    <!--                    <small>History</small>-->
                </span>
                <span style="float: right;"><input type="button" value="Go back" class="btn btn-effect-ripple btn-primary" onclick="window.location.href = './getProcessFlows.action'; goback();"></input></span>
<!--                <div style=""
                <h1>
                    Process Flow
                                        <small>History</small>
                </h1>
                <div align="right"><input type="button" value="Go back" class="btn btn-effect-ripple btn-primary" onclick="goback();"></input></div>-->
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-remove"></i>History</a></li>
                                    <li class="active">Archive History</li>
                                </ol>-->
            </section>
            

            <section class="content">

                <div class="box box-primary">
                    <div class="box-header with-border">
                        <div class="box-tools pull-right">

                        </div>
                    </div>  
                    <div class="box-body">
                        <div id="text">

                            <div style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>

                           
                                    <div class="form-group">
                                        <s:hidden id="docTpName" name="docTpName" value="%{docTpName}"/>
                                            <s:hidden id="docTpId" name="docTpId" value="%{docTpId}"/>
                                                 <s:hidden id="docTpSenderId" name="docTpSenderId" value="%{docTpSenderId}"/>
                                                      <s:hidden id="docTpReceiverId" name="docTpReceiverId" value="%{docTpReceiverId}"/>
                                                           <s:hidden id="docTransactionType" name="docTransactionType" value="%{docTransactionType}"/>
                                                                <s:hidden id="docStatus" name="docStatus" value="%{docStatus}"/>
                                        <s:hidden name="processId" value="%{processId}" id='processId' />
                                        <input type="hidden"   id='operatorId' />
                                        <!--Map Modal -->
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal" style="display: none" id="nodeModalButton">nodeModalButton</button>
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-md">
    
      <!-- Modal content-->
      <div class="modal-content  panel-primary">
      <div class="panel-heading" >
                   <b>Details</b>
                   <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
        
        <div class="modal-body">
        <div class='row'>
        <div  class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Title</label>
     
      <input class="form-control" id="title" type="text" >
    </div>
    </div>
    <div class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Status</label>
     
   
      <select class="form-control " id="mapStatus">
        <option value='Y'>Active</option>
        <option value='N'>InActive</option>
       
      </select>
      <input class="form-control" id="outputs" type="hidden">
      <input class="form-control" id="inputs" type="hidden">
    </div>
    </div>
     <!--   <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">Inputs</label>
      <input class="form-control" id="inputs" type="number">
    </div>
    </div>
    <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">Outputs</label>
      <input class="form-control" id="outputs" type="number">
    </div>
    </div> -->
    <div id="mapTypeDiv" class='col-sm-6'>
        <div class="form-group ">
      <label for="inputdefault">Map Type</label>
     <s:select name="mapType" id="mapType" cssClass="form-control" list="{'Translation','Extraction'}"  theme="simple" />
    </div>
    </div>
       </div>
           
    <div class='row'>
     <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">Sequence</label>
      <input class="form-control" id="sequence" type="number" min="1">
    </div>
    </div>
    
     <div  class='col-sm-6'>
     <div class="form-group ">
      <label for="inputdefault">Map<font style="color:red">*</font></label>
     
  <!-- <input class="form-control" id="map" type="text" ng-keyup="getEmployeesList()" ng-model="map.searchKey" onchange= "fieldLengthValidator(this)"> -->
    <input class="form-control" id="map" type="text" ng-keyup="getMapList()" ng-model="map.searchKey">
    </div>
    <div id="load1"  style=" color: red;display:none;font: bold;">Loading...
    </div>
    <div id="suggestionList1" class="suggestionList1" >
	<div ng-repeat="map in mapList" ng-model="consultant" ng-click="selectMap(map)">
	<a href="" class="suggestionList">{{map.name}} </a>
	</div>
</div>

    
    </div>
 
 <!--    </div>
     <div class='row'> -->
     <div class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Multiple</label>
     
   
      <select class="form-control " id="multiple">
        <option value='NO'>No</option>
        <option value='YES'>Yes</option>
       
      </select>
    </div>
    </div>
    
    
     </div>      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id='generateNode'>Update</button>
        </div>
      </div>
      
    </div>
  </div>
  <!-- Model end -->
  
  
  <!--Source Modal -->
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#sourceModal" style="display: none" id="sourceModalButton">sourceModalButton</button>
  <div class="modal fade" id="sourceModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content  panel-primary">
      <div class="panel-heading" >
                   <b>Details</b>
                   <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
        
        <div class="modal-body">
        <div class='row'>
        <div  class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Process Name</label>
     
      <input class="form-control" id="process_name" type="text" onchange= "fieldLengthValidator(this)">
    </div>
    </div>
     <div  class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Direction</label>
     
   
      <select class="form-control " id="direction" onchange="setLookupAlias();">
        <option value='INBOUND'>INBOUND</option>
        <option value='OUTBOUND'>OUTBOUND</option>
       
      </select>
    </div>
    </div>
      <div class='col-sm-3'>
        <div class="form-group ">
      <label for="inputdefault">Status</label>
     
   
      <select class="form-control " id="status">
        <option value='Y'>Active</option>
        <option value='N'>InActive</option>
       
      </select>
    </div>
    </div>
    <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">Connections</label>
      <input class="form-control" id="connections" type="number" min="1">
    </div>
    </div>
       </div>
         <div class='row'>
         <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">TP Name<font style="color:red">*</font></label>
     
    <input class="form-control" id="tpName" placeholder="Name or Id" type="text" ng-keyup="getTpList()" ng-model="tp.searchKey">
    </div>
        <div id="load4"  style=" color: red;display:none;font: bold;">Loading...
    </div>
    <div id="suggestionList4" class="suggestionList1" >
	<div ng-repeat="tp in TpList" ng-model="consultant" ng-click="selectTpName(tp)">
	<a href="" class="suggestionList">{{tp.name}} </a>
	</div>
    </div>
    </div>
    <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">TP Id<font style="color:red">*</font></label>
      <input class="form-control" id="tpId" type="text" onchange= "fieldLengthValidator(this)" readonly="true">
    </div>
    </div>
        
         <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">TP Sender Id<font style="color:red">*</font></label>
      <input class="form-control" id="tpSenderId" type="text" onchange= "fieldLengthValidator(this)">
    </div>
    </div>
    <div class='col-sm-3'>
     <div class="form-group">
      <label for="inputdefault">TP Receiver Id<font style="color:red">*</font></label>
      <input class="form-control" id="tpReceiverId" type="text" onchange= "fieldLengthValidator(this)">
    </div>
    </div>
         </div>
         <div class='row'>
         <div class='col-sm-4'>
     <div class="form-group">
      <label for="inputdefault">Transaction Type<font style="color:red">*</font></label>
      <input class="form-control" id="transactionType" type="text" onchange= "fieldLengthValidator(this)">
    </div>
    </div>
    <div class='col-sm-4'>
     <div class="form-group">
      <label for="inputdefault">Lookup Alias<font style="color:red">*</font></label>
      <input class="form-control" id="lookupAlias" type="text"  value="NA" readOnly onchange= "fieldLengthValidator(this)">
    </div>
    </div>
    <div class='col-sm-4'>
     <div class="form-group">
      <label for="inputdefault">Source Mail Box<font style="color:red">*</font></label>
      <!--<input class="form-control" id="sourceMailBox" type="text" onchange= "fieldLengthValidator(this)"> -->
    <input class="form-control" id="sourceMailBox" type="text" ng-keyup="getSourceMailBoxList()" ng-model="source.searchKey">
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
            <div class='row'>
        <div  class='col-sm-12'>
        <div class="form-group ">
      <label for="inputdefault">Description</label>
     
    <textarea class="form-control" rows="3" id="description" onchange= "fieldLengthValidator(this)"></textarea>
    </div>
    </div>
    </div>
    
           <div class='row'>
        <div  class='col-sm-12'>
        
    </div>
    </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id='generateSourceNode'>Update</button>
        </div>
      </div>
      
    </div>
  </div>
  <!-- source Model end -->
  
  <!-- Target model -->
  
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#targetModal" style="display: none" id="targetModalButton">targetModalButton</button>
  <div class="modal fade" id="targetModal" role="dialog">
    <div class="modal-dialog modal-md">
    
      <!-- Modal content-->
      <div class="modal-content  panel-primary">
      <div class="panel-heading" >
                   <b>Details</b>
                   <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
        
        <div class="modal-body">
        <div class='row'>
        <div  class='col-sm-6'>
        <div class="form-group ">
      <label for="inputdefault">Title</label>
     
      <input class="form-control" id="targetTitle" type="text">
    </div>
    </div>
    <div class='col-sm-6'>
        <div class="form-group ">
      <label for="inputdefault">Status</label>
     
   
      <select class="form-control " id="tagetStatus">
        <option value='Y'>Active</option>
        <option value='N'>InActive</option>
       
      </select>
    </div>
    </div>
    </div>
      <div class='row'>
      <!-- <div class='col-sm-6'>
     <div class="form-group">
      <label for="inputdefault">Inputs</label>
      <input class="form-control" id="targetInputs" type="number">
    </div>
    </div> -->
     <div class='col-sm-12'>
     <div class="form-group">
      <label for="inputdefault">Target Mail Box <font style="color:red">*</font></label>
      <!--<input class="form-control" id="targetMailBox" type="text" onchange= "fieldLengthValidator(this)"> -->
       <input class="form-control" id="targetMailBox" type="text" ng-keyup="getTargetMailBoxList()" ng-model="target.searchKey">
      <input class="form-control" id="targetInputs" type="hidden">
    </div>
         <div id="load3"  style=" color: red;display:none;font: bold;">Loading...
    </div>
    <div id="suggestionList3" class="suggestionList1" >
	<div ng-repeat="mailBox2 in mailBoxList2" ng-model="consultant" ng-click="selectTargetMailBox(mailBox2)">
	<a href="" class="suggestionList">{{mailBox2.name}} </a>
	</div>
</div>
    </div>
       </div>
           
    
          
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id='generateTargetNode'>Update</button>
        </div>
      </div>
      
    </div>
  </div>
  <!-- Target model end content-->

                                        <div class="row">
                                            <div class="col-sm-2 sidenav">
                                                <br>
                                                <ul class="nav nav-pills nav-stacked">

                                                    <li class="active"><a id="process_node">Source Node</a></li>

                                                    <li class="active"><a id="map_node">Map Node</a></li>
                                                    <!-- <li class="active"><a id="script_node">Script Node</a></li> -->
                                                    <li class="active"><a id="target">Target Node</a></li>

                                                    <!-- <button class="btn btn-primary" id="create_operator">Create A New Operator</button> -->
                                                    <li class="active"> <a class="btn-danger" id="delete_selected_button">Delete Selected&nbsp Node</a></li><!-- <button class="btn btn-danger" id="delete_selected_button">Delete Selected&nbsp Node&nbsp;</button></li> --> 

                                                    <li class="active"><a id="get_data">GetData </a></li> 
                                                    <!-- <li  class="active"><a href="javascript:showSourceModal();">Model</a></li>  -->
                                                    <!--    <li class="active"><a  id="Propertiesbtn">Properties</a></li>
                                                      <li  class="active"><a href="javascript:openModel();">Model</a></li> -->
                                                    <!--   <li class="active">  <a href="#" data-toggle="modal" data-target="#myModal" onclick='showModal();'>Model1</a></li> -->
                                                    <!--  <li class="active">  
                                                     <a href="#"   onclick='showModal();'>Model1</a> 
                                                     
                                                     </li>  -->
                                                </ul>

                                            </div>


                                            <div class="col-sm-10">

                                                <br>
                                                <div class="row">

                                                    <div class="col-sm-10">
                                                        <span id="load" style=" color: red;display:none;font: bold;">Loading...</span>
                                                        <div class="alert alert-success alert-dismissable fade in" style="display:none">
                                                            <a href="#" class="close" data-dismiss="alert" aria-label="close" style="padding-right: 22px;">&times;</a>
                                                            <span id="resultMessage">resultMessage</span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-2">
                                                    </div>

                                                </div>
                                                <div class="row">

                                                    <!-- left side pannel -->
                                                    <div class="col-md-12" id="flowDiv">
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading" id="accordion" style="height:42px">
                                                                <div class="col-md-11">
                                                                    Process Flow
                                                                </div>
                                                                <div class="col-md-1">
                                                                    <%--  <% if(request.getAttribute("processId")==null ){ %>  --%>
                                                                    <button class="btn btn-success btn-sm" id="saveFlow">Save</button>
                                                                    <%--  <%} %>  --%>
                                                                </div> 
                                                            </div>

                                                            <div class="panel-body" style="height:300px;overflow: scroll;" id="div1" ondrop="drop(event)" ondragover="allowDrop(event)">
                                                                <div class="demo" id="processFlow"></div>
                                                            </div>


                                                        </div>
                                                    </div>


                                                    <!-- right side pannel -->
                                                    
                                                </div>

                                                                <div class="row">
                                                                    <div class="col-md-12" style="display:none;" id="PropertiesDiv">
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading" id="accordion" style="height:42px">
                                                                <div class="col-md-11"  id="propertiesTitle">
                                                                    Properties
                                                                </div>  
                                                                <div class="col-md-1">
                                                                    <span id='closePropertiesDiv' class="glyphicon glyphicon-remove"></span>
                                                                </div> 
                                                            </div>

                                                            <div class="panel-body" style="height:320px;overflow-y: scroll;" id="div2" ondrop="drop1(event)" ondragover="allowDrop(event)">

                                                                <table  id="tblProperties" class="table">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>Name</th>
                                                                            <th>Value</th>

                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>

                                                                    </tbody>
                                                                </table>

                                                            </div>


                                                        </div>
                                                    </div>
                                                                </div>







                                                <br><br>




                                            </div>



                                        </div>

                                    


                                </div>
                            </div>
                        </div></div></div>
            </section>
            <div id="gridDiv">  


            </div>

        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>


        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>

        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
         
        <script src="http://code.jquery.com/jquery-1.12.2.min.js"></script>
        <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script> 
        <script src='<s:url value="../includes/js/processFlow/jquery.flowchart.js?version=7"/>'></script>

        <script src='<s:url value="../includes/js/processFlow/processFlow.js?version=11"/>'></script>
        <link rel="stylesheet" href='<s:url value="/includes/css/processFlow/jquery.flowchart.css?version=1.0"/>' type="text/css">
        <script type="text/javascript">
            function goback()
            {   
                 var a = document.getElementById("docTpName").value;
                 
                 var b = document.getElementById("docTpId").value;
                 var c = document.getElementById("docTpSenderId").value;
                 var d = document.getElementById("docTpReceiverId").value;
                 var e = document.getElementById("docTransactionType").value;
                 var f = document.getElementById("docStatus").value;
                
                window.location.href = './getProcessFlows.action?docTpName='+a+"&docTpId="+b+"&docTpSenderId="+c+"&docTpReceiverId="+d+"&docTransactionType="+e+"&docStatus="+f;
                
            
            }
                                         $(document).ready(function () {
                                                                            var data = {
                                                                                /*  ,
                                                                                 links: {
                                                                                 link_1: {
                                                                                 fromOperator: 'operator1',
                                                                                 fromConnector: 'output_1',
                                                                                 toOperator: 'operator2',
                                                                                 toConnector: 'input_2',
                                                                                 },
                                                                                 }*/
                                                                            };

                                                                            // Apply the plugin on a standard, empty div...
                                                                            $('#processFlow').flowchart({
                                                                                data: data
                                                                            });
                                                                        });
                                                                        var operatorI = 0;
                                                                      

                                                                        // for process creation
                                                                        var processId = 1;
                                                                        $('#process_node').click(function () {
                                                                            var operatorId = 'process' + processId;
                                                                            operatorId = generateOperatorId('process', processId);
                                                                            console.log('process_node ' + operatorId);
                                                                            var leftSize = 10 * processId * 5;
                                                                            var operatorData = {
                                                                                top: 60,
                                                                                left: leftSize,
                                                                                properties: {
                                                                                    title: 'Source Node ' + (opertorTitleId),
                                                                                    processId: 0,
                                                                                    direction: 'INBOUND',
                                                                                    status: 'Y',
                                                                                    tpName: '',
                                                                                    tpId: '',
                                                                                    tpSenderId: '',
                                                                                    tpReceiverId: '',
                                                                                    transactionType: '',
                                                                                    sourceMailBox: '',
                                                                                     lookupAlias : 'NA',
                                                                                    description: '',
                                                                                    outputs: {
                                                                                        output_1: {
                                                                                            label: 'Connection1',
                                                                                        }
                                                                                    }
                                                                                }
                                                                            };

                                                                            processId++;

                                                                            $('#processFlow').flowchart('createOperator', operatorId, operatorData);
                                                                        });

                                                                        // for map creation
                                                                        var MapId = 1;
                                                                        $('#map_node').click(function () {
                                                                            var operatorId = 'map' + MapId;
                                                                            operatorId = generateOperatorId('map', MapId);
                                                                            var leftSize = 10 * operatorId * 10;
                                                                            var operatorData = {
                                                                                top: 60,
                                                                                left: 300,
                                                                                properties: {
                                                                                    title: 'Map Node ' + (opertorTitleId),
                                                                                    mapId: 0,
                                                                                    status: 'Y',
                                                                                    sequence: '1',
                                                                                    map: '',
                                                                                    multiple: 'NO',
                                                                                    mapType: 'Translation',
                                                                                    inputs: {
                                                                                        input_1: {
                                                                                            label: 'In',
                                                                                        }
                                                                                    },
                                                                                    outputs: {
                                                                                        output_1: {
                                                                                            label: 'Out',
                                                                                        }
                                                                                    }
                                                                                }
                                                                            };

                                                                            MapId++;

                                                                            $('#processFlow').flowchart('createOperator', operatorId, operatorData);
                                                                        });


                                                                       
                                                                       
                                                                        // for outbound creation
                                                                        var outboundId = 1;
                                                                        $('#target').click(function () {


                                                                            var operatorId = 'target' + outboundId;
                                                                            operatorId = generateOperatorId('target', outboundId);
                                                                            var operatorData = {
                                                                                top: 80,
                                                                                left: 400,
                                                                                properties: {
                                                                                    title: 'Target Node ' + (opertorTitleId),
                                                                                    targetId: 0,
                                                                                    status: 'Y',
                                                                                    targetMailBox: '',
                                                                                    inputs: {
                                                                                        input_1: {
                                                                                            label: 'In',
                                                                                        }
                                                                                    }
                                                                                }
                                                                            };

                                                                            outboundId++;

                                                                            $('#processFlow').flowchart('createOperator', operatorId, operatorData);
                                                                        });

                                                                        $('#get_data').click(function () {
                                                                            var data = $('#processFlow').flowchart('getData');
                                                                            //  var title=  $("#processFlow").flowchart('getOperatorTitle','Outbound1' );
                                                                            //   alert(title)
                                                                            console.log(JSON.stringify(data, null, 2));
                                                                        });


                                                                        $('#delete_selected_button').click(function () {
                                                                            $('#processFlow').flowchart('deleteSelected');
                                                                        });

                                                                        $('#Propertiesbtn,#closePropertiesDiv').click(function () {
                                                                          /*  var flowDivClass = $('#flowDiv').attr('class');

                                                                            if (flowDivClass == 'col-md-12') {
                                                                                $("#flowDiv").fadeIn("slow").removeClass("col-md-12");
                                                                                $("#flowDiv").fadeIn("slow").addClass("col-md-8");
                                                                            } else {

                                                                                $("#flowDiv").fadeIn("slow").removeClass("col-md-8");
                                                                                $("#flowDiv").fadeIn("slow").addClass("col-md-12");
                                                                            }*/
                                                                            $("#PropertiesDiv").toggle(500);


                                                                           
                                                                        });



        </script>
        <script type="text/javascript">

                    function checkValues() {
                        var date = document.arcHisForm.reportrange.value;
                        var transType = document.getElementById("transType").value;
                        if (date == "")
                        {
                            alert("Please enter Date Range !!");
                            return false;
                        }
                        if (transType == "-1")
                        {
                            alert("Please select Transaction Type !!");
                            return false;
                        }
                    }
                    function Date1()
                    {
                        var date = document.arcHisForm.reportrange.value;
                        var arr = date.split("-");
                        var x = arr[1].trim();
                        document.getElementById("datepickerfrom").value = arr[0];
                        document.getElementById("datepicker").value = x;
                    }

                    function resetvalues()
                    {
                        document.getElementById('reportrange').value = "";
                        document.getElementById('transType').value = "-1";
                        //document.getElementById("datepickerfrom").value = "";
                        //document.getElementById("datepicker").value = "";

                        $('#gridDiv').hide();
                    }
//                     function goBack()
//                     {
//                                window.history.go(-1)
//                     }
            
        </script>
       <script>

                    $(document).ready(function () {


            <% if (request.getAttribute("processId") != null && !request.getAttribute("processId").toString().equals("")) { %>
                   
                        getExistedFlow();
                        
            <%}%>
                 $(".sidebar-toggle").click(function(){
                   if($('body').hasClass( "sidebar-collapse" )){
                       $('body').removeClass( "sidebar-collapse" )
                   }else{
                       $('body').addClass( "sidebar-collapse" )
                   }
                });

                    $(".treeview").click(function(){
                   
                    var d=$(this);
                    
                
                    if(d.children("ul").hasClass( "menu-open" )){
                         d.children("ul").removeClass("menu-open");
                         d.removeClass("active");
                    }else{
                     d.children("ul").addClass("menu-open");
                     d.addClass("active");
                 }
                }
                    );
                    });

        </script>
        
    </body>

</html>
