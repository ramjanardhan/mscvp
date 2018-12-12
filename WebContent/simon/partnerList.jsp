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
         <script type="text/javascript">
            function doOnLoad() {
                $('#simonscreen').addClass('active');
                $('#partnerList').addClass('active');
                $('#partnerList i').addClass('text-red');
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

        </script>


        <!--<script>
            $(function () {
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "order": [[0, 'desc']]
                });
            });
        </script>-->
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
    %>
     <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini">        
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
                <h1>
                    Partner List
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Document Repository</li>
                                </ol>-->
            </section>
            
            <section class="content">
                <div class="box box-primary">
<!--                    <div class="box-header with-border"></div> -->
                        <!--<b>  Basic Search </b>-->
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
                                                <div class="row">
                                                    <div class="col-sm-2"> <label>Partner Name</label>
                                                        <s:textfield name="partnername"  id="partnername" cssClass="form-control pull-left" value="%{partnername}" tabindex="1"/> 
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="2" /> 
                                                    </div>
                                                
                                                    <div  class="col-sm-2">
                                                        <label>Internal Identifier</label>  
                                                        <s:textfield name="internalidentifier"  id="internalidentifier" cssClass="form-control pull-left" value="%{internalidentifier}" tabindex="3"/> 
                                                    </div>

                                                    <div  class="col-sm-2">
                                                        <label>Partner Identifier</label>  
                                                        <s:textfield name="partneridentifier"  id="partneridentifier" cssClass="form-control pull-left" value="%{partneridentifier}" tabindex="4"/> 
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <label>Application Id</label>
                                                        <s:textfield name="applicationid"  id="applicationid" cssClass="form-control pull-left" value="%{applicationid}" tabindex="5"/> 
                                                    </div>
                                                      <div class="col-sm-2">
                                                        <label>Email</label>
                                                        <s:textfield name="email"  id="email" cssClass="form-control pull-left" value="%{email}" tabindex="6"/> 
                                                    </div>
                                                </div>
                                                
                                                <div class="row">
                                                  <div class="col-sm-2">
                                                        <label for="ackStatus">Country Code</label>
                                                        <s:textfield name="countrycode"  id="countrycode" cssClass="form-control pull-left" value="%{countrycode}" tabindex="7"/> 
                                                    </div>  
                                                    
                                                    <div class="col-sm-2 pull-left" style="width: 13.66666667%;margin-top: 30px;" >
<!--                                                        <div style="visibility: hidden;">For Space</div>-->
                                                        <input type="checkbox" name="wildcards" tabindex="8" value="wildcards" checked><label>Use WildCards</label>  
                                            </div> 
                                                    
                                                    <div class="col-sm-2  pull-left" style="width: 13.66666667%;margin-top: 30px;">
<!--                                                        <div style="visibility: hidden;">For Space</div>-->
                                                <input type="checkbox" name="ignorecase" tabindex="9" value="ignorecase" checked><label>Ignore Case</label> 
                                            </div> 
                                                 <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>                                               
                                                    

                                                </div>
                                                   

                                            </div>
                                        </div>
                                        <div class="row">
                                            
                                          <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="10"></strong></div>
                                            <div class="col-sm-2"> <strong><input type="button" value="Export" class="btn btn-primary col-sm-12" tabindex="11"></strong></div>
                                            <div class="col-sm-2"> <strong><input type="button" value="Export All" class="btn btn-primary col-sm-12" tabindex="12"></strong></div>
                                            <div class="col-sm-2 pull-left"><s:submit value="Search"   id="hideshow"   cssClass="btn btn-primary col-sm-12" tabindex="13"/></div>

                                            </td>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        </s:form>      

                                        </div>
                                        
                                        <span id="span1">
                                        </span>
<!--                                        <div class="row">
                                            
                                    </div>-->
                                </div>
                            </div>
                        </div></div>

                    <!-- Control Sidebar -->
                    <!-- /.control-sidebar -->
                    <!-- Add the sidebar's background. This div must be placed
                         immediately after the control sidebar -->
                    <!-- ./wrapper -->
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

                                                            <thead>
                                                                    <th >Action</th>
                                                                    <th >Partner&nbsp;Name</th> 
                                                                    <th >Internal&nbsp;Identifier</th>
                                                                    <th >Partner&nbsp;Identifier</th>
                                                                    <th >Application&nbsp;Id</th>
                                                                    <th >Email</th>
                                                                    <th >Country&nbsp;Code</th>
                                                                    <th >Status</th>
                                                                    <th >Created&nbsp;Date</th>
                                                                    <th >Changed&nbsp;Date</th>
                                                                    <th >Changed&nbsp;User</th>
                                                                

                                                                    <tbody><tr>
                                                                  <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>  

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                    <td>Test
                                                                    </td>

                                                                        </tr>
                                                                
                                                               
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
                        <%--</s:if>--%>

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
            $("#hideshow").click(function () {
                $("#hideshow1").show();
                return false;
            });

        </script>
    </body>
</html>
