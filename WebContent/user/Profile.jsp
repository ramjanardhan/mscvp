<%-- 
    Document   : Profile
    Created on : Jun 16, 2016, 11:08:43 AM
    Author     : miracle
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">

        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">

        <link rel="stylesheet" href='<s:url value="/includes/plugins/plugins/datepicker/datepicker3_1.css"/>' type="text/css">

        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

        <script src='<s:url value="../includes/plugins/datepicker/bootstrap-datepicker.js"/>'></script>

        <script language="JavaScript"
        src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script type="text/javascript">
            $('#dob').datepicker();
        </script>
        <script type="text/javascript">
            function doOnLoad()
            {
                $("#profile").addClass("active");
                $("#profile i").addClass("text-red");

                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            function fileValidation() {
                var imagePath = document.imageForm.imagePath;
                alert(imagePath);
                if (imagePath.value != null && (imagePath.value != "")) {
                    document.imageForm.imagePath.focus();
                    return (true);
                }

                return (false);
            }

            function imageValidation() {
                //imageUpdate
                var imageUpdate = document.getElementById("imageUpdate").value;
                if (imageUpdate == '') {
                    document.getElementById('imageResultMessage').innerHTML = "<font color=red>Please select file.</font>";
                    return false;
                }
            }

//            function passwordValidation() {
//                alert("inside password change");
//                //var oldPwd = document.getElementById("oldPwd").value;
//                var oldPwd = document.forms["resetPwdForm"]["oldPwd"].value;
//               // alert("oldPwd "+document.resetPwdForm.getElementById("oldPwd").value);
//                alert(oldPwd);
//                var pwd = document.getElementById("newPwd").value;
//                alert("pwd is "+pwd);
//                var cnfPwd = document.getElementById("confirmPwd").value;
//                alert("cnfpwd is "+cnfPwd);
//                if (oldPwd == "") {
//                     alert("in oldpwd");
//                    document.getElementById('pwdResultMessage').innerHTML = "<font color=red>Please enter Old Password</font>";
//                    return false;
//                }
//                if (pwd == "") {
//                    alert("in pwd");
//                    document.getElementById('pwdResultMessage').innerHTML = "<font color=red>Please enter Password</font>";
//                    return false;
//                }
//                if (cnfPwd == "") {
//                     alert("in cnfpwd");
//                    document.getElementById('pwdResultMessage').innerHTML = "<font color=red>Please enter Confirm Password</font>";
//                    return false;
//                }
//                if (pwd != cnfPwd) {
//                    document.getElementById('pwdResultMessage').innerHTML = "<font color=red>Passwords not matched</font>";
//                    document.getElementById("regpassword").value = "";
//                    document.getElementById("cnfPassword").value = "";
//                    return false;
//                }
//                return false;
//            }

            function validateForm() {
                var education = document.forms["profileForm"]["education"].value;
                var designation = document.getElementById('designation').value;
                var location = document.forms["profileForm"]["location"].value;
                var organization = document.forms["profileForm"]["organization"].value;
                var dob = document.forms["profileForm"]["dob"].value;
                var gender = document.forms["profileForm"]["gender"].checked;
                var phonenumber = document.forms["profileForm"]["phonenumber"].value;
                if (education.length == 0 || education == "" || education == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your education.</font>";
                    // document.getElementById('education').style.borderColor = "red";
                    return (false);
                } else if (designation.length == 0 || designation == "" || designation == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your designation.</font>";
                    // document.getElementById('designation').style.borderColor = "red";
                    return (false);
                } else if (location.length == 0 || location == "" || location == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your location.</font>";
                    // document.getElementById('location').style.borderColor = "red";
                    return (false);
                } else if (organization.length == 0 || organization == "" || organization == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your organization.</font>";
                    // document.getElementById('organization').style.borderColor = "red";
                    return (false);
                    //document.getElementById("reportCheckbox").checked = true;
                } else if (dob.length == 0 || dob == "" || dob == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your date of birth.</font>";
                    // document.getElementById('dob').style.borderColor = "red";
                    return (false);
                } else if (gender == false) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please select gender.</font>";
                    //  document.getElementById('gender').style.borderColor = "red";
                    return (false);
                } else if (phonenumber.length == 0 || phonenumber == "" || phonenumber == null) {
                    document.getElementById('resultMessage').innerHTML = "<font color=red>Please enter your phonenumber.</font>";
                    //  document.getElementById('phonenumber').style.borderColor = "red";
                    return (false);
                }
            }
            ;

            function formatPhone(element) {
                str = new String(element.value);
                element.value = str.replace(/[A-Za-z\(\)\.\-\x\s,]/g, "");
                        num = element.value;
                var _return;
                if (num.length == 10) {
                    _return = "(";
                    var ini = num.substring(0, 3);
                    _return += ini + ")";
                    var st = num.substring(3, 6);
                    _return += "-" + st + "-";
                    var end = num.substring(6, 10);
                    _return += end;
                    element.value = "";
                    element.value = _return;
                } else {
                    if (num.length > 10) {
                        document.getElementById('resultMessage').innerHTML = "<font color=red>Phone Number should be 10 characters.</font>";
                        element.value = _return;
                        element.value = "";
                        element.focus();
                        return false;
                    } else {
                        if (num.length < 10) {
                            document.getElementById('resultMessage').innerHTML = "<font color=red>Please give atleast  10 charcters in PhoneNumber.</font>";
                            element.value = "";
                        }
                    }
                }
                return _return;
            }

            function ValidateFileUpload() {
                var file = $("#imageUpdate").val();

                if (file == '') {
                    $("imageErrorMsg").html("<font color='red'>Please Upload an image</font>");
                    return false;
                }

                if (file != '')
                {
                    var size = document.getElementById('imageUpdate').files[0].size;
                    var leafname = file.split('\\').pop().split('/').pop();
                    var extension = file.substring(file.lastIndexOf('.') + 1);
                    if (extension == "jpg" || extension == "png" || extension == "gif") {
                        var size = document.getElementById('imageUpdate').files[0].size;
                        if (leafname.length > 30) {
                            document.getElementById('imageUpdate').value = '';
                            $("imageErrorMsg").html("<font color='red'>File name length must be less than 30 characters!</font>");
                            // document.getElementById('InsertContactInfo').innerHTML = "<font color=red>File name length must be less than 30 characters!</font>"
                            // showAlertModal("File size must be less than 2 MB");
                            return false;
                        }
                        else
                        {
                            if (parseInt(size) < 2097152) {
                                $("imageErrorMsg").html("");
                            } else {
                                document.getElementById('imageUpdate').value = '';
                                $("imageErrorMsg").html("<font color='red'>File size must be less than 2 MB</font>");
                                // document.getElementById('InsertContactInfo').innerHTML = "<font color=red>File size must be less than 2 MB</font>"
                                // showAlertModal("File size must be less than 2 MB");
                                return false;
                            }
                        }
                    }
                    else
                    {
                        document.getElementById('imageUpdate').value = "";
                        //document.getElementById('InsertContactInfo').innerHTML = "<font color=red>Invalid file extension!Please select pdf or doc or docx or gif or jpg or png or jpeg file.</font>"
                        $("imageErrorMsg").html("<font color='red'>Invalid file extension!<br> Please select gif or jpg or png file</font>");
                        // $("#InsertContactInfo").html(" <font color=red>Invalid file extension! Please select gif or jpg or png file</font>");
                        return false;
                    }
                }
                $("#imageErrorMsg").html("");
                return true;
            }

            function removeValue() {
                document.getElementById("imageUpdate").value = "";
            }
            function abc(x) {
                if(x.length<8){
                    alert('Password must be 8 to 15 characters');
                }if(x.length>15){
                    alert('Password must be less than or equal to 15 characters');
                }
               // alert(x.length);
            }
        </script>
        <style>
            .inner-addon { 
                position: relative; 
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
            #LoadingContent>img{
                width:150px;
            }

        </style>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/bootstrap.css"/>' type="text/css"> 
        <script type="text/javascript" src='<s:url value="../includes/plugins/datepicker/moment.js"/>'></script>
    </head>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>



        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <section class="content-header">
                <h1>
                    User Profile
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                    <li><a href="#">Profile</a></li>
                    <li class="active">User Profile</li>
                </ol>-->
            </section>

            <!-- Main content -->
            <section class="content">

                <div class="row">

                    <div class="col-md-12">
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#about-me" data-toggle="tab">About Me</a></li>
                                <li><a href="#avatar" data-toggle="tab">Avatar</a></li>
                                <li><a href="#change-password" data-toggle="tab">Change Password</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="active tab-pane" id="about-me">
                                    <!-- Post -->
                                    <div class="post">

                                        <div class="row">
                                            <%-- <s:form name="profileForm" action="profileUpdate" theme="simple" method="post"> --%>
                                            <s:form action="profileUpdate" theme="simple" method="post" name="profileForm" onsubmit="return validateForm();">
                                                <div class="col-sm-12">

                                                    <div style="text-align: center;" id="resultMessage"></div><br>
                                                    <div style="text-align: center;">${resultMessage}</div>
                                                    <%--  <s:property value="#request.resultMessage"/> --%>

                                                    <div class="row">
                                                        <div class="col-sm-2"> <label for="name">Name</label></div>
                                                        <div class="col-sm-4"> <s:textfield name="name"  id="name" placeholder="Nina Mcintire" cssClass="form-control pull-left"   value="%{userBean.name}"  readonly="true" tabindex="1"/> 
                                                        </div> 
                                                        <div class="col-sm-2"> <label for="education">Education</label></div>
                                                        <div class="col-sm-4">  <s:textfield name="education"  id="education" placeholder=" B.S. in Computer Science from the University of Tennessee at Knoxville" cssClass="form-control pull-left"   value="%{userBean.education}" tabindex="2" />  
                                                        </div> 
                                                    </div> <br>
                                                    <div class="row">
                                                        <div class="col-sm-2"> <label for="designation">Designation</label></div>
                                                        <div class="col-sm-4">  <s:textfield name="designation"  id="designation" placeholder="Software Engineer" cssClass="form-control pull-left"   value="%{userBean.designation}"  tabindex="3"/>  
                                                        </div>
                                                        <div class="col-sm-2">  <label for="location">Location</label> </div>
                                                        <div class="col-sm-4">   <s:textfield name="location"  id="location" placeholder="Novi MI" cssClass="form-control pull-left"   value="%{userBean.location}" tabindex="4" />  
                                                        </div>
                                                    </div><br>
                                                    <div class="row">
                                                        <div class="col-sm-2"> <label for="organization">Organization</label> </div>
                                                        <div class="col-sm-4">  <s:textfield name="organization"  id="organization" placeholder="Miracle Software Systems, Inc." cssClass="form-control pull-left"   value="%{userBean.organization}"  tabindex="5"/>
                                                        </div>
                                                        <div class="col-sm-2"> <label for="dob">DOB</label> </div>
                                                        <div class="col-sm-4"><div class="inner-addon left-addon"><s:textfield name="dob" data-provide="datepicker"  id="dob" cssClass="form-control pull-left" placeholder="MM/DD/yyyy"  value="%{userBean.dob}" style="padding: 0px 34px;" tabindex="6"  /><i class="fa fa-calendar glyphicon glyphicon-calendar" style="position: relative;right: -9px;top: -26px;"></i></div> 
                                                                <%-- <div class="col-sm-4"><div class="inner-addon"><s:textfield name="dob" data-provide="datepicker"  id="dob" cssClass="form-control pull-left" placeholder="MM/DD/yyyy"  value="%{userBean.dob}"  /></div> --%>

                                                        </div>

                                                    </div>


                                                    <div class="row">
                                                        <div class="col-sm-2"><label for="gender">Gender</label></div> 
                                                        <div class="col-sm-4" style="padding:13px">
                                                            <s:radio name="gender" id="gender" cssClass="width:30px" list="{'Male','Female'}" value="%{userBean.gender}" tabindex="7"></s:radio>
                                                            </div>

                                                            <div class="col-sm-2">
                                                                <label>Phone Number</label>
                                                            </div>
                                                            <div class="col-sm-4">
                                                            <s:textfield name="phonenumber"  id="phonenumber" placeholder="9123456780" cssClass="form-control pull-left"   value="%{userBean.phonenumber}" onchange="formatPhone(this)" tabindex="8" />  
                                                        </div>
                                                    </div> <br>

                                                    <div class="row">

                                                        <div class="col-sm-2"><b> <s:submit value="Update" cssClass="btn btn-primary btn-block" tabindex="9"/></b></div>
                                                        <div class="col-sm-6"></div>
                                                        <div class="col-sm-4"></div>
                                                    </div>
                                                </div>
                                            </s:form>
                                        </div><!-- /.post --> 
                                    </div>

                                </div><!-- /.tab-pane -->
                                <div id="loadingAcoountSearch" class="loadingImg">
                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                </div>
                                <div class="tab-pane" id="avatar">
                                    <!-- The timeline -->
                                    <div class="row">
                                        <div class="col-sm-4"></div>
                                        <div class="col-sm-4">
                                            <div style="text-align: center;" id="imageResultMessage"></div>
                                            <s:form name="imageForm" action="imageUpload" theme="simple" method="post" enctype="multipart/form-data">
                                                <div style="text-align: center;">${resultMessage}</div>
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <center> 
                                                            <s:url id="uImage" action="renderImage" namespace="/user"> </s:url>
                                                            <img src="<s:property value="#uImage" />" width="100" height="100" class="img-circle" alt="User Image" tabindex="1"/>
                                                            <!--                                                        <img src="../bootstrap/img/user2-160x160.jpg" class="img-circle" alt="User Image"> --></center>
                                                    </div>
                                                </div> <br>
                                                <div class="row">
                                                    <center>   
                                                        <div class="col-sm-12">
                                                            <div class="input-group"> 
                                                                <!--                                                      IMAGE:</td><td><input type="file" name="image" id="image"/>-->
                                                                <!--                                                            <input class="form-control" id="fileDestination" name="fileDestination" placeholder="Upload" readonly required="required" type="text">-->
                                                                <span><imageErrorMsg></imageerrormsg></span>
                                                                        <s:file name="imageUpdate" cssClass="btn btn-primary btn-block btn-flat" id="imageUpdate" onchange="ValidateFileUpload();" tabindex="2"/>
                                                            </div>
                                                        </div> </center> </div> <br>
                                                <div class="row">
                                                    <div class="col-sm-6">
                                                        <strong>   <input type="button" value="Remove"   class="btn btn-primary col-sm-12" onclick="removeValue();" tabindex="3"/></strong>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <s:submit value="Upload" onclick="return imageValidation()"  cssClass="btn btn-primary col-sm-12" tabindex="4"/>
                                                    </div>
                                                </div>
                                            </s:form>
                                        </div>
                                    </div>
                                    <div class="col-sm-4"></div> 
                                    <br>


                                </div><!-- /.tab-pane -->


                                <div class="tab-pane" id="change-password">
                                    <br>
                                    <div style="text-align: center;" id="pwdResultMessage"></div>
                                    <s:form class="form-horizontal" action="../user/updateMyPwd.action" method="post" name="resetPwdForm" id="resetPwdForm" theme="simple">
                                        <div style="text-align: center;">${resultMessage}</div>
                                        <div class="row">
                                            <div class="form-group" >
                                                <label for="oldpassword" class="col-sm-2">Old Password<font style="color:red">*</font></label>
                                                <div class="col-sm-4">
                                                    <s:password cssClass="form-control" placeholder="Old Password" name="oldPwd" id="oldPwd" tabindex="1" />
                                                </div> 
                                                <div class="col-sm-3"></div>
                                                <div class="col-sm-3"></div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="form-group">
                                                <label for="newpassword" class="col-sm-2">New Password<font style="color:red">*</font></label>
                                                <div class="col-sm-4">
                                                    <s:password cssClass="form-control" placeholder="New Password" name="newPwd" id="newPwd" tabindex="2" />
                                                </div>
                                                <div class="col-sm-3">(New Password should be 3 to 15 characters)</div>
                                                <div class="col-sm-3"></div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="form-group">
                                                <label for="confirmpassword" class="col-sm-2">Confirm Password<font style="color:red">*</font></label>
                                                <div class="col-sm-4">
                                                    <s:password cssClass="form-control" placeholder="Confirm Password" name="confirmPwd" id="confirmPwd" tabindex="3" />
                                                </div>
                                                <div class="col-sm-3">(Enter New Password again)</div>
                                                <div class="col-sm-3"></div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-sm-2"><s:submit value="Update" cssClass="btn btn-primary col-sm-12" tabindex="4"/></div>

                                            <div class="col-sm-6"></div>
                                            <div class="col-sm-4"></div>
                                        </div>
                                    </s:form>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- /.nav-tabs-custom -->
                    </div><!-- /.col -->
                </div><!-- /.row -->

            </section><!-- /.content -->
        </div><!-- /.content-wrapper -->

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <!-- AdminLTE App -->
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>


    </body>
</html>

