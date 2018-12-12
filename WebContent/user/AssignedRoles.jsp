
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@page import="com.mss.ediscv.util.AppConstants"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>


<html>
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <sx:head cache="true"/>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad()
            {
                $("#admintree").addClass("active");
                $("#searchusr").addClass("active");
                $("#searchusr i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            function validateFlow() {
                var manufacturing = document.getElementById("manufacturing").checked;
                var logistics = document.getElementById("logistics").checked;
                var docvisibility = document.getElementById("docvisibility").checked;
                var primaryFlow = document.getElementById("primaryFlow").value;
                if (manufacturing == false && logistics == false && docvisibility == false) {
                    alert('Please select at least one Assigned Flow');
                } else if (manufacturing == true && logistics == true && docvisibility == true) {
                    return true;
                } else if (manufacturing == true && logistics == true) {
                    if (primaryFlow == '5') {
                        alert('Please select Manufacturing/Logistics as Primary Flow');
                        return false;
                    }
                } else if (manufacturing == true && docvisibility == true) {
                    if (primaryFlow == '3') {
                         alert('Please select Manufacturing/DocumentVisibility as Primary Flow');
                        return false;
                    }
                } else if (logistics == true && docvisibility == true) {
                    if (primaryFlow == '2') {
                        alert('Please select Logistics/DocumentVisibility as Primary Flow');
                        return false;
                    }
                } else if (manufacturing == true) {
                    if (primaryFlow != '2') {
                        alert('Please select Manufacturing as Primary Flow');
                        return false;
                    }
                } else if (logistics == true) {
                    if (primaryFlow != '3') {
                        alert('Please select Logistics as Primary Flow');
                        return false;
                    }
                } else if (docvisibility == true) {
                    if (primaryFlow != '5') {
                        alert('Please select DocumentVisibility as Primary Flow');
                        return false;
                    }
                }
            }
            $(function() {
                $('#attach_box').click(function() {
                    $('#sec_box').show();
                    return false;
                });
            });
            $(function() {
                $('#detail_link').click(function() {
                    $('#detail_box').show();
                    return false;
                });
            });

            $('.check_link').click(function()
            {
                var thisCheck = $(this);
                if (thischeck.is(':checked'))
                {
                    $('#check_box').show();
                }
            });
            // New function to show the left grid

            function getDetails(val) {
                getPoDetails(val);
            }
            function getList()
            {
                // alert('hello');
                // tradingPartnerForm.action =  "../tp/getTpList.action";
                //tradingPartnerForm.submit();
            }
            function checkUserValues() {


                var fname = document.getElementById("fname").value;
                var lname = document.getElementById("lname").value;
                var email = document.getElementById("email").value;
                var ophno = document.getElementById("ophno").value;
                var status = document.getElementById("status").value;
                var role = document.getElementById("role").value;
                var deptId = document.getElementById("deptId").value;

                //if(((fname!=null)&&(fname!=""))&&((lname!=null)&&(lname!=""))&&((email!=null)&&(email!=""))&&((ophno!=null)&&(ophno!="")))
                if ((fname == null) || (fname == "")) {
                    alert("Please enter First Name!");
                    return false;
                }
                if ((lname == null) || (lname == "")) {
                    alert("Please enter Last Name!");
                    return false;
                }
                if ((email == null) || (email == "")) {
                    alert("Please enter Email!");
                    return false;
                }
                if ((ophno == null) || (ophno == "")) {
                    alert("Please enter Office Number!");
                    return false;
                }
                if ((status == null) || (status == "-1")) {
                    alert("Please select status!");
                    return false;
                }
                if ((role == null) || (role == "-1")) {
                    alert("Please select role!");
                    return false;
                }
                if ((deptId == null) || (deptId == "-1")) {
                    alert("Please select Department!");
                    return false;
                }
                else
                {
                    return true;
                }
            }

            function resetvalues()
            {
                document.getElementById('fname').value = "";
                document.getElementById('lname').value = "";
                document.getElementById('email').value = "";
                document.getElementById('ophno').value = "";
                document.getElementById('status').value = "-1";
                document.getElementById('role').value = "-1";
                document.getElementById('deptId').value = "-1";
                //$('#detail_box').hide();
                //$('#gridDiv').hide();

            }

            function getContact(detail)
            {
                // alert("in contact"+detail);

                $(function() {

                    $('#detail_box').show();
                    return false;

                });
                var details = detail;
                var detailsDIV = document.getElementById("detailInformation");
                detailsDIV.innerHTML = "";
                detailsDIV.innerHTML = details;


            }
            function getUserList() {
                // var tpid = document.getElementById("tpid").value;
                // var tpname = document.getElementById("tpname").value;
                location.href = "../user/backToSearchList";
                return true;
            }
            function init() {
                if (document.getElementById("userPageId").value != '0') {
                    document.getElementById("email").readOnly = true;
                }
            }
            function getUserList() {
                // var tpid = document.getElementById("tpid").value;
                // var tpname = document.getElementById("tpname").value;
                location.href = "../user/backToSearchList";
                return true;
            }
        </script>
        <style>
            .form-control1
            {
                width:  10px;
                height: 10px;
            }
            .loadingImg {
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
                position: fixed;
                display: block;
                opacity: 0.7;
                background-color: #9999C2;
                z-index: 99;
                text-align: center;
            }

            #LoadingContent {
                position: absolute;
                top: 50%;
                left: 50%;
                z-index: 100;
            }
            #LoadingContent > img{
                width:150px;
            }

        </style>

    </head>
    <body class="hold-transition skin-blue sidebar-mini"  onload="doOnLoad()">

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
                    Assign Flows
                    <%
                        if (request.getAttribute("resultMessage") != null) {
                            String reqponseString = request.getAttribute("resultMessage").toString();
                            out.println(reqponseString);
                        }
                    %>

                    <%String contextPath = request.getContextPath();
                    %>
                    <small>Pages</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-user"></i>Pages</a></li>
                    <li class="active"> Assign Flows</li>
                </ol>
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
                            <div class="box-header with-border">
                                <div class="box-tools pull-right">
                                </div>
                            </div><!-- /.box-header -->
                            <div class="box-body">
                                <div id="text">
                                    <!-- /.col (right) -->
                                    <!-- First Row -->
                                    <div class="row">

                                        <div class="col-sm-12">
                                            <s:form name="assingnFlow" action="transferFlow" theme="simple">    
                                                <br>
                                                <s:hidden name="userId" value="%{userBean.userId}"/>
                                                <div class="row">
                                                    <div class="col-sm-3"> <label for="username"> User Name </label>

                                                        <s:textfield name="username"  id="username" cssClass="form-control"   value="%{userBean.name}" readonly="true" /> 

                                                        <s:hidden name="userName" value="%{userBean.name}"/>
                                                    </div>
                                                    <div class="col-sm-3"> <label for="loginid"> Log In ID </label>

                                                        <s:textfield name="loginid"  id="loginid" cssClass="form-control"   value="%{userBean.loginId}" readonly="true"  />
                                                        <s:hidden name="loginId" value="%{userBean.loginId}"/>
                                                    </div>
                                                    <div class="col-sm-3"><label for="primaryrole">Primary Role</label>
                                                        <s:textfield name="primaryrole"  id="primaryrole" cssClass="form-control"   value="%{userBean.primaryRole}" readonly="true" />

                                                    </div>

                                                    <div class="col-sm-3"> <label for="primaryFlow">Primary Flow<font style="color:red">*</font></label>

                                                        <s:select list="primaryFlowsList" name="primaryFlow" id="primaryFlow" value="%{userBean.primaryFlow}" cssClass="form-control"   />
                                                    </div>

                                                </div>

                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label> Assigned Flows<font style="color:red">*</font></label>
                                                    </div>
                                                    <div class="col-sm-3">

                                                        <s:checkbox name="logistics" id="logistics" label="Logistics" cssClass="form-control1"/> <label for="logistics">Logistics</label><br>

                                                        <s:checkbox name="manufacturing" id="manufacturing" label="Manufacturing" cssClass="form-control1"/>  <label for="manufacturing">Manufacturing</label>  <br>
                                                        <s:checkbox name="docvisibility" id="docvisibility" label="Document Visibility" cssClass="form-control1"/> <label for="docvisibility">Document Visibility</label>  
                                                    </div>
                                                </div>
                                                <div class="col-sm-3"></div>
                                            </div>
                                        </div>




                                        <div class="row">
                                            <div class="col-sm-2">
                                                <!--                  <button type="button" class="btn btn-primary col-sm-12">Save</button>-->
                                                <s:submit   value="Save" cssClass="btn btn-primary col-sm-12" tabindex="16" onclick="return validateFlow()"/>

                                            </div>
                                            <div class="col-sm-2"> 
                                                <strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getUserList();" tabindex="9"/></strong>
                                            </div>
                                            <div class="col-sm-8"></div>
                                        </s:form>

                                    </div>
                                </div>
                            </div><!-- /.box-body -->
                        </div><!-- /.box -->

                        <!--box-->
                    </div>

                </div>
                <div class="col-sm-1"></div>

            </section>
            <!--main content-->
        </div><!-- /.content-wrapper -->
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script type="text/javascript">
            function retailer()
            {
                //alert("clicked on retailer");
                //  document.getElementById("list").value="retalier";
                var li = document.getElementById("list").value;

                li.style.background = "red";
            }
            function set()
            {
                var rslt = document.getElementById("list").value;
                //alert(rslt);
                document.getElementById("list1").innerHTML = rslt;

            }

        </script>


        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    </body>
</html>

