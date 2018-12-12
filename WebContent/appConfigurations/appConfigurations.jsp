<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<%@page import="com.mss.ediscv.appConfigurations.AppConfigurationsBean"%>
<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function doOnLoad() {
                $("#appConstants").addClass("active");
                $("#utilities").addClass("active");
                $("#appConstants").css('font-weight', 'bold');
                $("#appConstants i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>
        <style>
            .row{
                padding-left: 15px;
            }
            span{
                cursor: pointer;
            }
            .labelColor{
                color: #00aae7;
            }
            /*            .modalLabels{
                            text-align: center;
                            padding: 10px;
                        }*/
        </style>
    </head>
    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini fixed">    

        <div>
            <s:include value="/includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="/includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <!-- Main content --> 
            <section class="content-header">
                <h1> App Configurations </h1>
            </section>
            <section class="content">
                <div class="box box-primary">
                    <div class="box-body">
                        <div id="text">
                            <div style="alignment-adjust:central;">
                                <%String contextPath = request.getContextPath();%>
                                <s:form action="" method="post" name="documentForm" id="documentForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <center><span id="ResultMessage"></span></center>
                                                <br>
                                                <div class="row">
                                                    <div  class="col-sm-3">
                                                        <label class="labelColor">Time Intervals &nbsp; : &nbsp;&nbsp;&nbsp;</label><span style="color: #00aae7">(In minutes)</span>
                                                    </div>
                                                    <div  class="col-sm-2">
                                                        <s:select cssClass="form-control" list="#@java.util.LinkedHashMap@{'15':'15','30':'30'}" name="timeInterval" id="timeInterval" value="%{timeInterval}" style="width:100px;" onchange="changeTimeinterval()" />
                                                    </div>
                                                    <div class="col-sm-1">
                                                        <span class="glyphicon glyphicon-info-sign" title="Select time interval for EDI/RAIL TRANS/ HOUR on DAIILY STATS VIEW "></span>
                                                    </div>
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div  class="col-sm-3">
                                                        <label class="labelColor">Top 10 Manufacturing TP &nbsp; :</label>
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <span style="color: blue" class="glyphicon glyphicon-edit" title="Click To Edit" data-toggle="modal" data-target="#top10EDITPModal"></span>
                                                    </div>
                                                    <div class="col-sm-1">
                                                        <span class="glyphicon glyphicon-info-sign" title="Enter Top 10 EDI Trading Partners for TP Business View Tab."></span>
                                                    </div>
                                                    <div  class="col-sm-3">
                                                        <label class="labelColor">Top 10 Logistic TP &nbsp; :</label> 
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <span style="color: blue" class="glyphicon glyphicon-edit" title="Click To Edit" data-toggle="modal" data-target="#top10RAILTPModal"></span>
                                                    </div>
                                                    <div class="col-sm-1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <span class="glyphicon glyphicon-info-sign" title="Enter Top 10 RAIL Trading Partners for TP Business View Tab."></span>
                                                    </div>
                                                </div>
                                                <br>
                                                
                                                <div class="row">
                                                     <div  class="col-sm-3">
                                                        <label class="labelColor">Transactions Set for Dashboard &nbsp; :</label>
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <span style="color: blue" class="glyphicon glyphicon-edit" title="Click To Edit" data-toggle="modal" data-target="#TransactionsForDashboardsModal"></span>
                                                    </div>
                                                    <div class="col-sm-1">
                                                        <span class="glyphicon glyphicon-info-sign" title="Enter Transaction Set need to be visible in Daily Stats View & Business Flow View."></span>
                                                    </div>
                                                    <div  class="col-sm-3">
                                                        <label class="labelColor">Database &nbsp; :</label>
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <span style="color: blue" class="glyphicon glyphicon-edit" title="Click To Edit" data-toggle="modal" data-target="#dataBaseModal"></span>
                                                    </div>
                                                    <div class="col-sm-1">
                                                        <span class="glyphicon glyphicon-info-sign" title="Enter Database Properties."></span>
                                                    </div>
                                                   
                                                </div>
                                                <br>
                                               
                                            </div>
                                        </div>

                                        <div id="loadingAcoountSearch" class="loadingImg">
                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                        </div>
                                    </s:form>
                                </div>
                            </div>
                        </div>
                    </div>
                <!--</div>  -->
            </section>

        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <!--Edit App Constant-->
        <div class="modal fade" id="top10EDITPModal" role="dialog">
            <div class="modal-dialog modal-lg"  style="width:1000px;">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;height:50px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:white;text-align: center;">Top 10 Manufacturing Trading partners</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <s:form action="" method="post" name="topEDITP" id="topEDITP" theme="simple">
                                <center><span id="Top10EDIResultMessage"></span></center>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>1 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP1" id="ediTP1" value="%{ediTP1}" tabindex="1" onchange="CheckTP(this)"/>
                                            </div>

                                            <div  class="col-sm-1">
                                                <label>2 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP2" id="ediTP2" value="%{ediTP2}" tabindex="2" onchange="CheckTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>3 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP3" id="ediTP3" value="%{ediTP3}" tabindex="3" onchange="CheckTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>4 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP4" id="ediTP4" value="%{ediTP4}" tabindex="4" onchange="CheckTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>5 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP5" id="ediTP5" value="%{ediTP5}" tabindex="5" onchange="CheckTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>6 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP6" id="ediTP6" value="%{ediTP6}" tabindex="6" onchange="CheckTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>7 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP7" id="ediTP7" value="%{ediTP7}" tabindex="7" onchange="CheckTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>8 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP8" id="ediTP8" value="%{ediTP8}" tabindex="8" onchange="CheckTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>9 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP9" id="ediTP9" value="%{ediTP9}" tabindex="9" onchange="CheckTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>10 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="ediTP10" id="ediTP10" value="%{ediTP10}" tabindex="10" onchange="CheckTP(this)"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-info btn-sm" onclick="changeTop10EDITP();">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Update Config
                                    </button>
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="top10RAILTPModal" role="dialog">
            <div class="modal-dialog modal-lg" style="width:1000px;">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;height:50px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:white;text-align: center;">Top 10 Logistic Trading partners</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <s:form action="" method="post" name="topRailTP" id="topRailTP" theme="simple">
                                <center><span id="Top10RailResultMessage"></span></center>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>1 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP1" id="railTP1" value="%{railTP1}" tabindex="1" onchange="CheckRAILTP(this)"/>
                                            </div>

                                            <div  class="col-sm-1">
                                                <label>2 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP2" id="railTP2" value="%{railTP2}" tabindex="2" onchange="CheckRAILTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>3 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP3" id="railTP3" value="%{railTP3}" tabindex="3" onchange="CheckRAILTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>4 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP4" id="railTP4" value="%{railTP4}" tabindex="4" onchange="CheckRAILTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>5 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP5" id="railTP5" value="%{railTP5}" tabindex="5" onchange="CheckRAILTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>6 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP6" id="railTP6" value="%{railTP6}" tabindex="6" onchange="CheckRAILTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>7 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP7" id="railTP7" value="%{railTP7}" tabindex="7" onchange="CheckRAILTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>8 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP8" id="railTP8" value="%{railTP8}" tabindex="8" onchange="CheckRAILTP(this)"/>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div  class="col-sm-1">
                                                <label>9 :</label> 
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP9" id="railTP9" value="%{railTP9}" tabindex="9" onchange="CheckRAILTP(this)"/>
                                            </div>
                                            <div  class="col-sm-1">
                                                <label>10 :</label>
                                            </div>
                                            <div  class="col-sm-5">
                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="tpList" name="railTP10" id="railTP10" value="%{railTP10}" tabindex="10" onchange="CheckRAILTP(this)"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-info btn-sm" onclick="changeTop10RailTP();">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Update Config
                                    </button>
                                </div>
                            </s:form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="dataBaseModal" role="dialog">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;height:50px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:white;text-align: center;">Database Properties</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <s:form action="" method="post" name="scvp_DB_Form" id="scvp_DB_Form" theme="simple">
                                <center><span id="dbResultMessage"></span></center> 
                                <div class="row" id="dbDiv">
                                    <div class="col-sm-12">
                                        <div class="row">
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Property Name</label> 
                                            </div>
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Property Value</label> 
                                            </div>
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Display Order</label> 
                                            </div>
                                        </div>
                                        <%if (session.getAttribute("appConfigurationsDBList") != null) {
                                                java.util.List appConfigurationsDBCList = new java.util.ArrayList();
                                                AppConfigurationsBean appConfigurationsDBBean = null;
                                                appConfigurationsDBCList = (java.util.List) session.getAttribute("appConfigurationsDBList");
                                        %>   
                                        <input type="hidden" class="form-control" value="<%=appConfigurationsDBCList.size()%>" id="dbExistCount" name="dbExistCount"/>
                                        <%      for (int j = 0; j < appConfigurationsDBCList.size(); j++) {
                                                appConfigurationsDBBean = (AppConfigurationsBean) appConfigurationsDBCList.get(j);
                                        %><div class="row">
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsDBBean.getPropertyName()%>" id="dbPropertyName<%=j%>" name="dbPropertyName<%=j%>"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsDBBean.getPropertyValue()%>" id="dbPropertyValue<%=j%>" name="dbPropertyValue<%=j%>"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsDBBean.getDisplayOrder()%>" id="dbDisplayOrder<%=j%>" name="dbDisplayOrder<%=j%>"  onkeyup="isNumeric(this);"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <button type="button" class="btn btn-danger btn-sm" onclick="dbRemoveButton(this);
                                                        RemoveAppConfData('SCVP_DATABASE', '<%=appConfigurationsDBBean.getPropertyId()%>');" id="dbDisplayCount<%=j%>" name="dbDisplayCount<%=j%>">Delete </button>
                                            </div>
                                        </div>
                                        <% }
                                            } %>
                                    </div>
                                </div>    
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-info btn-sm" id="addRowDB" name="addRowDB">
                                        <span class="fa fa-plus"></span> Add
                                    </button>
                                    <button type="button" class="btn btn-info btn-sm" onclick="changeScvpDbValues();">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Update Config
                                    </button>
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="TransactionsForDashboardsModal" role="dialog">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;height:50px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:white;text-align: center;">Transactions for Dashboards</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <s:form action="" method="post" name="transactionsForm" id="transactionsForm" theme="simple">
                                <center><span id="TransactionNamesResultMessage"></span></center>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="row">
                                            <div  class="col-sm-2">
                                                <label>Manufacturing </label> 
                                            </div>
                                            <div class="col-sm-3">
                                                <s:textarea cssClass="form-control" name="ediTransactionNamesList" id="ediTransactionNamesList" value="%{ediTransactionNamesList}" style="width: 500px;height: 80px" tabindex="1"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div  class="col-sm-2">
                                                <label>Logistic </label> 
                                            </div>
                                            <div class="col-sm-3">
                                                <s:textarea cssClass="form-control" name="railTransactionNamesList" id="railTransactionNamesList" value="%{railTransactionNamesList}" style="width: 500px;height: 80px" tabindex="1"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-info btn-sm" onclick="changeTransactionNames();">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Update Config
                                    </button>
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="BaseValuesModal" role="dialog">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header" style="background-color: #00aae7;height:50px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title" style="color:white;text-align: center;">Transaction Base Value</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <s:form action="" method="post" name="baseValueForm" id="baseValueForm" theme="simple">
                                <center><span id="baseValueResultMessage"></span></center>
                                <div class="row" id="baseValueDiv">
                                    <div class="col-sm-12">
                                        <div class="row">
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Transaction Names</label> 
                                            </div>
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Value</label> 
                                            </div>
                                            <div  class="col-sm-3">
                                                <label class="modalLabels"> Display Order</label> 
                                            </div>
                                        </div>
                                        <%if (session.getAttribute("appConfigurationsBaseValueList") != null) {
                                                java.util.List appConfigurationsBaseValueList = new java.util.ArrayList();
                                                AppConfigurationsBean appConfigurationsBaseValueBean = null;
                                                appConfigurationsBaseValueList = (java.util.List) session.getAttribute("appConfigurationsBaseValueList");
                                        %>  
                                        <input type="hidden" class="form-control" value="<%=appConfigurationsBaseValueList.size()%>" id="baseValueExistCount" name="baseValueExistCount"/>
                                        <%     for (int j = 0; j < appConfigurationsBaseValueList.size(); j++) {
                                                appConfigurationsBaseValueBean = (AppConfigurationsBean) appConfigurationsBaseValueList.get(j);
                                        %><div class="row">
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsBaseValueBean.getPropertyName()%>" id="baseValuePropertyName<%=j%>" name="baseValuePropertyName<%=j%>"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsBaseValueBean.getPropertyValue()%>" id="baseValuePropertyValue<%=j%>" name="baseValuePropertyValue<%=j%>"  onkeyup="isNumeric(this);"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" value="<%=appConfigurationsBaseValueBean.getDisplayOrder()%>" id="baseValueDisplayOrder<%=j%>" name="baseValueDisplayOrder<%=j%>" onkeyup="isNumeric(this);"/>
                                            </div>
                                            <div class="col-sm-3">
                                                <button type="button" class="btn btn-danger btn-sm" onclick="baseValueRemoveButton(this);
                                                        RemoveAppConfData('BASE_VALUE', '<%=appConfigurationsBaseValueBean.getPropertyId()%>');" id="baseValueDisplayCount<%=j%>" name="baseValueDisplayCount<%=j%>">Delete </button>
                                            </div>
                                        </div>
                                        <% }
                                            }%>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-info btn-sm" id="addRowBaseValue" name="addRowBaseValue">
                                        <span class="fa fa-plus"></span> Add
                                    </button>
                                    <button type="button" class="btn btn-info btn-sm" onclick="changebaseValueValues();">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Update Config
                                    </button>
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js?version=1.3"/>'></script>
        <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="/includes/bootstrap/js/menuScroll.js?version=1.2"/>'></script>
        <script type="text/javascript">
                                        

                                        //Database
                                        var dbExistCount = document.getElementById('dbExistCount').value;
                                        var dbcount = dbExistCount;
                                        var dbrmcount = 0;
                                        var dbrmElementsArray = new Array();
                                        $('#addRowDB').on('click', function () {
                                            $('#dbDiv').append(
                                                    ' <div class="row"><div class="col-sm-12">' +
                                                    '<div class="col-sm-3">' +
                                                    ' <input type="text" class="form-control" name="dbPropertyName' + dbcount + '" id="dbPropertyName' + dbcount + '" value="" tabindex="1"/>' +
                                                    '</div><div  class="col-sm-3">' +
                                                    ' <input type="text" class="form-control" name="dbPropertyValue' + dbcount + '" id="dbPropertyValue' + dbcount + '" value="" tabindex="1"/>' +
                                                    '</div><div  class="col-sm-3">' +
                                                    ' <input type="text" class="form-control" name="dbDisplayOrder' + dbcount + '" id="dbDisplayOrder' + dbcount + '" value="" tabindex="1"  onkeyup="isNumeric(this);"/>' +
                                                    ' </div><div  class="col-sm-3">' +
                                                    '<button  type="button" id="dbDisplayCount' + dbcount + '" name="dbDisplayCount' + dbcount + '" class="btn btn-warning"   style="margin-top:1px ;" onclick="dbRemoveButton(this);"><i class="fa fa-minus"></i></button>' +
                                                    ' </div>' +
                                                    '</div></div>');
                                            dbcount++;
                                        });
                                        function dbRemoveButton(x) {
                                            var rmelements = x.id;
                                            rmelements = rmelements.slice(14);
                                            document.getElementById('dbPropertyName' + rmelements).style.display = "none";
                                            document.getElementById('dbPropertyValue' + rmelements).style.display = "none";
                                            document.getElementById('dbDisplayOrder' + rmelements).style.display = "none";
                                            document.getElementById('dbDisplayCount' + rmelements).style.display = "none";
                                            dbrmElementsArray[dbrmcount] = {};              // creates a new object
                                            dbrmElementsArray[dbrmcount] = rmelements;
                                            dbrmcount++;
                                        }

                                        
                                        
                                        function baseValueRemoveButton(x) {
                                            var rmelements = x.id;
                                            rmelements = rmelements.slice(21);
                                            document.getElementById('baseValuePropertyName' + rmelements).style.display = "none";
                                            document.getElementById('baseValuePropertyValue' + rmelements).style.display = "none";
                                            document.getElementById('baseValueDisplayOrder' + rmelements).style.display = "none";
                                            document.getElementById('baseValueDisplayCount' + rmelements).style.display = "none";
                                            baseValuermElementsArray[baseValuermcount] = {};              // creates a new object
                                            baseValuermElementsArray[baseValuermcount] = rmelements;
                                            baseValuermcount++;
                                        }
                                        function CheckTP(currentTP) {
//                                            alert(currentTP.value);
//                                            alert(currentTP.id.slice(5))
                                            for (var x = 1; x <= 10; x++) {
                                                if (x == currentTP.id.slice(5)) {
                                                    document.getElementById("Top10EDIResultMessage").innerHTML = " ";
                                                } else {
                                                    if (currentTP.value == document.getElementById('ediTP' + x).value) {
//                                                        alert("already selected please select another TP");
                                                        document.getElementById("Top10EDIResultMessage").innerHTML = "<font color='red'>already selected please select another TP</font>";
                                                        document.getElementById('ediTP' + currentTP.id.slice(5)).value = "-1"
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        function CheckRAILTP(currentTP) {
//                                            alert(currentTP.value);
//                                            alert(currentTP.id.slice(5))
                                            for (var x = 1; x <= 10; x++) {
                                                if (x == currentTP.id.slice(6)) {
                                                    document.getElementById("Top10RailResultMessage").innerHTML = " ";
                                                } else {
                                                    if (currentTP.value == document.getElementById('railTP' + x).value) {
//                                                        alert("already selected please select another TP");
                                                        document.getElementById("Top10RailResultMessage").innerHTML = "<font color='red'>already selected please select another TP</font>";
                                                        document.getElementById('railTP' + currentTP.id.slice(6)).value = "-1"
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        function isNumeric(x) {
                                            if (!(x.value).match(/^\d+/)){
                                                alert("Please enter numeric characters only..!!");
                                                x.value ="";
                                            }
                                        }
        </script>
    </body>
</html>
