<%@page import="com.mss.ediscv.tp.TpBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.mss.ediscv.po.PurchaseOrderBean"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>


<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>
<%@page buffer="50kb" autoFlush="true" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />

        <%-- <meta name="description" content="website description" />
         <meta name="keywords" content="website keywords, website keywords" />
         <meta http-equiv="content-type" content="text/html; charset=UTF-8" />  --%>



        <script language="JavaScript"
        src='<s:url value="/includes/js/generalValidations.js"/>'></script>
        <script language="JavaScript"
        src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
       <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

        <style type="text/css">
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
        <%--        <script>
                    /* $(function() {
          $( "#podatepicker" ).datepicker();
               $( "#podatepickerfrom" ).datepicker();
        });
                     */
                    var myCalendar;
                    function doOnLoad() {
                        myCalendar = new dhtmlXCalendarObject(["podatepickerfrom","podatepicker"]);
                        myCalendar.setSkin('omega');
                        myCalendar.setDateFormat("%m/%d/%Y");
                        myCalendar.hideTime();
                                
                    }

            
        </script>--%>
        <script type="text/javascript"> 
            function doOnLoad() 
            {
                $("#admintree").addClass("active");
                $("#createusr").addClass("active");
                $("#createusr i").addClass("text-red");
                
                document.getElementById('loadingAcoountSearch').style.display="none";
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
   
            $('.check_link').click (function ()
            {
                var thisCheck = $(this);
                if (thischeck.is (':checked'))
                {
                    $('#check_box').show();
                }
            });
            // New function to show the left grid

            function getDetails(val){  
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
                if((fname==null)||(fname=="")) {
                    alert("Please enter First Name!");
                    return false;
                }
                if((lname==null)||(lname=="")) {
                    alert("Please enter Last Name!");
                    return false;
                }
                if((email==null)||(email=="")) {
                    alert("Please enter Email!");
                    return false;
                }
                if((ophno==null)||(ophno=="")) {
                    alert("Please enter Office Number!");
                    return false;
                }
                if((status==null)||(status=="-1")) {
                    alert("Please select status!");
                    return false;
                }
                if((role==null)||(role=="-1")) {
                    alert("Please select role!");
                    return false;
                }
                if((deptId==null)||(deptId=="-1")) {
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
                document.getElementById('fname').value="";
                document.getElementById('lname').value="";
                document.getElementById('email').value="";
                document.getElementById('ophno').value="";
                document.getElementById('status').value="-1";
                document.getElementById('role').value="-1";
                document.getElementById('deptId').value="-1";
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
                var detailsDIV=document.getElementById("detailInformation");
                detailsDIV.innerHTML="";
                detailsDIV.innerHTML=details;
   
   
            }
            function getUserList(){
                // var tpid = document.getElementById("tpid").value;
                // var tpname = document.getElementById("tpname").value;
                location.href = "../user/backToSearchList";
                return true;
            }
            function init() {
                if(document.getElementById("userPageId").value != '0' ) {
                    document.getElementById("email").readOnly = true;
                }
            }
        </script>


    </head>
    <body onload="doOnLoad()" class="hold-transition skin-blue sidebar-mini"> 
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>

        <div class="content-wrapper">
            <s:hidden name="userPageId" value="%{userPageId}" id="userPageId"/>
            <section class="content-header">
                <%
                    if (request.getAttribute("userPageId").toString().equals("0")) {
                %>
                <h1>User Creation</h1>
                <%} else {%>
                <h1>User Updation</h1>
                <%}%>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-user"></i>Admin</a></li>
                    <li class="active">User Creation</li>
                </ol>
            </section>
            <br>
            <section class="content">

                <div class="box box-primary">

                    <div class="box-body">
                        <div id="text">  




                            <div class="content" >
                                <div class="content_item" id="searchdiv">
                                    <%
                                        if (request.getAttribute("resultMessage") != null) {
                                            String reqponseString = request.getAttribute("resultMessage").toString();
                                            out.println(reqponseString);
                                        }
                                    %>
                                    <div style="alignment-adjust:central;" >
                                        <%String contextPath = request.getContextPath();
                                        %>

                                        <%-- ../user/createUser.action--%>
                                        <s:form action="%{currentAction}" method="post" name="userForm" id="userForm" theme="simple">
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <s:hidden name="id" value="%{id}"/>
                                                            <div class="col-sm-3"> <label>First Name<font color="red">*</font></label>
                                                                    <s:textfield name="fname"  id="fname" cssClass="form-control " maxLength="50"  value="%{fname}"  /> 
                                                            </div>   
                                                            <div class="col-sm-3"> <label>Last Name<font color="red">*</font></label>
                                                                    <s:textfield name="lname"  id="lname" cssClass="form-control "  maxLength="50" value="%{lname}"  /> 
                                                            </div>
                                                            <div class="col-sm-3"> <label>Email<font color="red">*</font></label>
                                                                    <s:textfield name="email"  id="email" cssClass="form-control"  maxLength="100" value="%{email}" onblur="return validateEmail();" /> 
                                                            </div>
                                                            <div class="col-sm-3"> <label>Office Phone<font color="red">*</font></label>
                                                                    <s:textfield name="ophno"  id="ophno" cssClass="form-control " maxLength="14" onchange="return formatPhone(this);" onblur="return validatenumber(this);"  value="%{ophno}"  /> 
                                                            </div>

                                                        </div> <br>                                                        <div class="row">
                                                            <div class="col-sm-3"> <label>Status<font color="red">*</font></label>
                                                                    <s:select name="status" id="status" headerKey="-1" headerValue="Select Status" list="#@java.util.LinkedHashMap@{'A':'Active','I':'Inactive','T':'Terminated'}"    cssClass="form-control " value="%{status}"  /> 
                                                            </div>  
                                                            <div class="col-sm-3"> <label>Role<font color="red">*</font></label>
                                                                    <s:select name="role" id="role" headerKey="-1" headerValue="Select Role" list="userRolesMap"    cssClass="form-control" value="%{role}"  /> 
                                                            </div>  
                                                            <div class="col-sm-3"> <label>Department<font color="red">*</font></label>
                                                                    <s:select name="deptId" id="deptId"  headerKey="-1" headerValue="Select Department" list=" #@java.util.LinkedHashMap@{'1':'Management','2':'Operations','3':'Developement'}"   cssClass="form-control" value="%{deptId}"  /> 
                                                            </div> 
                                                        </div>
                                                        <div id="loadingAcoountSearch" class="loadingImg">
                                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                        </div>
                                                        <br><br>
                                                        <div class="row">
                                                            <%
                                                                // out.println("pageId-->"+request.getAttribute("tppageId").toString());
                                                                if (request.getAttribute("userPageId").toString().equals("0")) {
                                                            %>
                                                            <div class="col-sm-2"><s:submit value="Create"  onclick="return checkUserValues();" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>

                                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="17" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>
                                                                    <%} else {%>
                                                            <div class="col-sm-2"> <s:submit value="Update" cssClass="btn btn-primary col-sm-12" onclick="return checkUserValues();" tabindex="8"/> </div>

                                                            <div class="col-sm-2"><strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getUserList();" tabindex="9"/></strong></div>
                                                                    <%}%>






                                                        </s:form>
                                                    </div>
                                                </div>
                                            </div></div>
                                    </div>
                                </div>  

                            </div>

                        </div>
                    </div>
            </section>



        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <!-- AdminLTE App -->
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
           </body>


</html>