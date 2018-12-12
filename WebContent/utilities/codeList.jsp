<%@page import="java.util.TimerTask"%>
<%@page import="java.util.Timer"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<%@page import="com.mss.ediscv.utilities.CodeListBean"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <%-- <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script> --%>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script>
            function doOnLoad() {
                $("#utilities").addClass("active");
                $("#codeList").addClass("active");
                $("#codeList i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            $(function () {
                if (document.getElementById("items").value == 0)
                {
                    $('#results').DataTable({
                        "paging": false,
                        "lengthChange": true,
                        "searching": false,
                        "ordering": false,
                        "info": true,
                        "autoWidth": false,
                       "scrollX": true,
                        "scrollY": 300

                    });
                }
                else
                {
                    $('#results').DataTable({
                        "paging": false,
                        "lengthChange": true,
                        "searching": false,
                        "ordering": false,
                        "info": true,
                        "autoWidth": false,
                        "scrollX": true,
                        "scrollY": 300
                    });
                }
            });

            var count = 0;
            $(document).ready(function () {
                window.setTimeout(function () {
                    // This will execute 5 seconds later
                    document.getElementById('messagediv').innerHTML = "";

                }, 5000);
                $('#add').click(function () {
                    var rowCount;
                    if (deletedRows.length != 0) {
                        rowCount = $('#results tr').length + deletedRows.length;
                    }
                    else {
                        rowCount = $('#results tr').length;
                    }
                    if (rowCount == 2)
                    {
                        $('#results').dataTable().fnDestroy();
                        count = $('#results tr').length;
                        for (i = 1; i < ($('#results tr').length + deletedRows.length); i++) {
                            var flag1 = checkArray(i);
                            if (flag1 == true)
                            {
                                count++;
                            }
                            else if (flag1 == false) {
                                continue;
                            }
                        }

                        $('#results').append(
                                '<tr id="' + count + '"><td><input type="checkbox" name="check' + count + '" id="check' + count + '" theme="simple" onclick="selectAllUncheck();selectAllCheck();"/></td>' +
                                '<td><input type="text" id="senderItem' + count + '" name="senderItem' + count + '"/><input type="hidden" value="" id="senderId' + count + '" name="senderId' + count + '"/><input type="hidden" value="" id="listName' + count + '" name="listName' + count + '"/></td>' +
                                '<td><input type="text" id="recItem' + count + '" name="recItem' + count + '" onchange="checkItems(' + count + ')"/></td>' +
                                '<td><input type="text" id="desc' + count + '" name="desc' + count + '"/></td>' +
                                '<td><input type="text" id="text1' + count + '" name="text1' + count + '"/></td>' +
                                '<td><input type="text" id="text2' + count + '" name="text2' + count + '"/></td><input type="hidden" value="" id="recId' + count + '" name="recId' + count + '"/>' +
                                '<td><input type="text" id="text3' + count + '" name="text3' + count + '"/></td><input type="hidden" value="" id="listVersion' + count + '" name="listVersion' + count + '"/>' +
                                '<td><input type="text" id="text4' + count + '" name="text4' + count + '"/></td>' +
                                '<td><input type="text" id="text5' + count + '" name="text5' + count + '"/></td>' +
                                '<td><input type="text" id="text6' + count + '" name="text6' + count + '"/></td>' +
                                '<td><input type="text" id="text7' + count + '" name="text7' + count + '"/></td>' +
                                '<td><input type="text" id="text8' + count + '" name="text8' + count + '"/></td>' +
                                '<td><input type="text"  id="text9' + count + '" name="text9' + count + '"/></td>' +
                                '</tr>');
                        // $('#results').dataTable();
                        $('#results').DataTable({
                            "paging": false,
                            "lengthChange": true,
                            "searching": true,
                            "ordering": false,
                            "info": true,
                            "autoWidth": false,
                            "scrollX": true,
                            "scrollY": 100
                        });
                    } else {
                        count = $('#results tr').length;
                        for (i = 1; i < ($('#results tr').length + deletedRows.length); i++) {
                            var flag1 = checkArray(i);
                            if (flag1 == true)
                            {
                                count++;
                            }
                            else if (flag1 == false) {
                                continue;
                            }
                        }
                        $('#results').append(
                                '<tr id="' + count + '"><td><input type="checkbox" name="check' + count + '" id="check' + count + '" theme="simple" onclick="selectAllUncheck();selectAllCheck();"/><input type="hidden" value="" id="listName' + count + '" name="listName' + count + '"/></td>' +
                                '<td><input type="text" id="senderItem' + count + '" name="senderItem' + count + '"/></td>' +
                                '<td><input type="text" id="recItem' + count + '" name="recItem' + count + '" onchange="checkItems(' + count + ')"/></td>' +
                                '<td><input type="text" id="desc' + count + '" name="desc' + count + '"/></td>' +
                                '<td><input type="text" id="text1' + count + '" name="text1' + count + '"/><input type="hidden" value="" id="senderId' + count + '" name="senderId' + count + '"/></td>' +
                                '<td><input type="text" id="text2' + count + '" name="text2' + count + '"/></td>' +
                                '<td><input type="text" id="text3' + count + '" name="text3' + count + '"/></td>' +
                                '<td><input type="text" id="text4' + count + '" name="text4' + count + '"/></td>' +
                                '<td><input type="text" id="text5' + count + '" name="text5' + count + '"/></td><input type="hidden" value="" id="recId' + count + '" name="recId' + count + '"/>' +
                                '<td><input type="text" id="text6' + count + '" name="text6' + count + '"/></td><input type="hidden" value="" id="listVersion' + count + '" name="listVersion' + count + '"/>' +
                                '<td><input type="text" id="text7' + count + '" name="text7' + count + '"/></td>' +
                                '<td><input type="text" id="text8' + count + '" name="text8' + count + '"/></td>' +
                                '<td><input type="text"  id="text9' + count + '" name="text9' + count + '"/></td>' +
                                '</tr>');
                    }
                    return false;
                });
            });

            $(function () {
                $('#clear').click(function () {
                    $('#results').DataTable().clear().draw();
                    $('#selectedName').val("");
                    $('#modifieddate').val("");
                    $('#items').val("");
                    $('#listName').val("-1");
                    $('#messagediv').empty();
                    $('#checkboxAll').attr("checked", false);
                    $('#newname').val("");
                    deletedRows = [];

                });
            });
        </script>
        <style>
            @media (min-width: 992px) and (max-width: 1192px) {
                #set_align1{
                    margin: 0 -13px !important;
                }
            }
            /*            .content-wrapper
                        {
                            min-height: 800px !important;
                        }*/
            div.dataTables_wrapper {
                width: 1000px;
                margin: 0 auto;
                overflow-x: hidden;
            }
            #site_content .f1{
                background-color:transparent;
                border: 0;

            }
            table.dataTable thead > tr > th{
                white-space: nowrap;
            }
            .c1 .btn{
                margin-top:5px;
                width: 100%;
            }

        </style>
<style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>


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
            <!-- Intro -->

            <section class="content-header">
                <h1>
                    Code List Editor
                    <!--<small>Code List</small>-->
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a ><i class="fa fa-gavel"></i>Code List Editor</a></li>
                    <li class="active">Code List</li>
                </ol>-->
            </section>
            <center> <div id="responseString">
                 <%--   <%
                        String responseString = "";
                        if (session.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                            responseString = session.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                            out.println(responseString);
                            session.setAttribute(AppConstants.REQ_RESULT_MSG, null);
                        }

                    %> --%>
                </div> 
            </center>
            <section class="content">
                <s:form action="../utilities/getCodeListName.action" method="post" cssClass="contact-form" name="certForm" id="certForm" theme="simple">

                    <div class="box box-primary">
                        <div class="box-body">

                            <div class="col-sm-3">
                                <label>Name Search</label>
                                <s:textfield name="name" id="name" cssClass="form-control" value="%{name}" tabindex="1"/> 
                            </div>
                            <div class="col-sm-3" style="padding-top: 25px"> 
                                <div>
                                    <s:submit value="Search" cssClass="btn btn-primary" id="button" name="button" tabindex="2" /> 
                                </div>
                            </div>
                            <div class="col-sm-3"> 
                                <label>Code List </label>
                                <s:select headerKey="-1" headerValue="--Select Type--" cssClass="form-control" list="listNameMap" name="listName" id="listName" value="%{listName}" onchange="getList();" tabindex="3"/> 
                            </div>
                            <s:hidden name="json" id="json"/>
                        </div> <br>
                    </div>


                   




                </s:form>
            </section>
            
            

            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <section class="content">
                <div id="site_content"> 
                    <div class="box box-primary">

                        <div class="box-body">
                            <div class="col-sm-12">
                                <div class="col-sm-4">
                                    <label >Code List Selected :&nbsp;</label>
                                    <s:textfield name="selectedName" id="selectedName" cssClass="f1" value="%{selectedName}" tabindex="4" readonly="true"/> 
                                </div>
                                <div class="col-sm-4">
                                    <label >Last Date Modified :&nbsp;</label>
                                    <s:textfield id="modifieddate" name="modifieddate"  cssClass="f1" value="%{modifieddate}" tabindex="5" readonly="true"/>
                                </div>
                                <div class="col-sm-4"><label class="pull-left">Number Of Code List Items : &nbsp;</label>
                                    <s:textfield id="items"  name="items" value="%{items}" cssClass="f1" style="width:60px" tabindex="6" readonly="true"/>
                                </div>
                            </div>
                            <br>
                            <div id="gridDiv"> 
                                <section class="content">
                                    <div class="row">
                                        <div class="col-md-12 col-sm-12 col-xs-12">
                                            <div class="box">
                                                <div class="box-header">
                                                    <h3 class="box-title"></h3>
                                                </div><!-- /.box-header -->
                                                <div class="box-body">
                                                    <%!String cssValue = "whiteStripe";
                                                        int resultsetTotal;%>
                                                    <div>
                                                        <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="background-color: white;">
                                                                    <table id="results" class="table table-bordered table-hover">
                                                                        <thead><tr>
                                                                                <th><input type="checkbox" id="checkboxAll" name="checkboxAll" onclick="selectAllRecords();" tabindex="7"/>&nbsp;SELECT</th>
                                                                                    <%-- <th>LIST_NAME <font class="text-danger">*</font></th> 
                                                                                     <th>SENDER_ID</th>
                                                                                     <th>RECEIVER_ID</th>
                                                                                     <th>LIST_VERSION</th> --%>
                                                                                <th>SENDER_CODE <font class="text-danger">*</font></th>
                                                                                <th>RECEIVER_CODE <font class="text-danger">*</font></th>
                                                                                <th>DESCRIPTION <font class="text-danger">*</font></th>
                                                                                <th>TEXT1</th>
                                                                                <th>TEXT2</th>
                                                                                <th>TEXT3</th>
                                                                                <th>TEXT4</th>
                                                                                <th>TEXT5</th>
                                                                                <th>TEXT6</th>
                                                                                <th>TEXT7</th> 
                                                                                <th>TEXT8</th> 
                                                                                <th>TEXT9</th> 
                                                                            </tr> </thead>
                                                                        <tbody>

                                                                            <%if (session.getAttribute("codeList") != null) {
                                                                                    java.util.List codeList = new java.util.ArrayList();
                                                                                    CodeListBean codeListBean = null;
                                                                                    codeList = (java.util.List) session.getAttribute("codeList");
                                                                                    System.out.println("list size is " + codeList.size());
                                                                                    int count;
                                                                                    for (int j = 0; j < codeList.size(); j++) {
                                                                                        codeListBean = (CodeListBean) codeList.get(j);


                                                                            %><tr> <td><input type="checkbox" id="check<%=j + 1%>" name="check<%=j + 1%>" onclick="selectAllUncheck();selectAllCheck(); "/></td>
                                                                                   
                                                                        <input type="hidden" value="<%=codeListBean.getListName()%>" id="listName<%=j + 1%>" name="listName<%=j + 1%>"/>
                                                                        <input type="hidden" value="<%=codeListBean.getSender_id()%>" id="senderId<%=j + 1%>" name="senderId<%=j + 1%>"/>
                                                                        <input type="hidden" value="<%=codeListBean.getReceiver_id()%>" id="recId<%=j + 1%>" name="recId<%=j + 1%>"/>
                                                                        <input type="hidden" value="<%=codeListBean.getList_version()%>" id="listVersion<%=j + 1%>" name="listVersion<%=j + 1%>"/>
                                                                        <td><input value="<%=codeListBean.getSender_item()%>" id="senderItem<%=j + 1%>" name="senderItem<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getReceiver_item()%>" id="recItem<%=j + 1%>" name="recItem<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getDescription()%>" id="desc<%=j + 1%>" name="desc<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText1()%>" id="text1<%=j + 1%>" name="text1<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText2()%>" id="text2<%=j + 1%>" name="text2<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText3()%>" id="text3<%=j + 1%>" name="text3<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText4()%>" id="text4<%=j + 1%>" name="text4<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText5()%>" id="text5<%=j + 1%>" name="text5<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText6()%>" id="text6<%=j + 1%>" name="text6<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText7()%>" id="text7<%=j + 1%>" name="text7<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText8()%>" id="text8<%=j + 1%>" name="text8<%=j + 1%>"/></td>
                                                                        <td><input value="<%=codeListBean.getText9()%>" id="text9<%=j + 1%>" name="text9<%=j + 1%>"/></td>
                                                                            <%
                                                                                    }
                                                                                }%>
                                                                        </tbody>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td align="right" colspan="28" style="background-color: white;">
                                                                    <div align="right" id="pageNavPosition"></div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <%-- <input type="hidden" name="sec_po_list" id="sec_po_list" value="<%=list.size()%>"/> --%>
                                                    </div>
                                                </div>
                                                <%-- Grid End --%>
                                            </div>
                                        </div>
                                    </div>
                                   
                                    <div class="col-sm-4 gnexcel-space">
                                        <center><div id="messagediv"></div> </center>
                                        <div class="col-sm-12"> 
                                            <label>New Code List Name</label>
                                            <s:textfield name="newname" id="newname" cssClass="form-control" value="%{newname}" tabindex="8"/> 
                                        </div>
                                        <div class="c1 col-sm-12">

                                            <input type="button" class="btn btn-primary" value="New CodeList SI Import" id="import" tabindex="9" onclick="insertRows();"/>

                                        </div>
                                    </div><br>
                                    <div class="col-sm-offset-4 col-sm-4 gnexcel-space">
                                        <div class="c1 col-sm-6"> 
                                            <input type="button" id="add" name="add" class="btn btn-primary" value="Add Row" tabindex="10"/>
                                        </div> 
                                        <%--<div class="c1 col-sm-4"> 
                                            <input type="button" class="btn btn-primary" value="Delete Row" id="deleteRow" tabindex="11" onclick="deleteRows();"/>
                                        </div>--%>

                                        <div class="c1 col-sm-6"> 
                                            <input type="button" id="clear" class="btn btn-primary" value="Clear Grid" tabindex="13"/>
                                        </div>
                                        <div class="c1 col-sm-12">
                                            <input type="button" class="btn btn-primary" value="Update Existing CodeList" id="update" tabindex="12" onclick="getUpdateRow()"/>
                                        </div>

                                    </div>




                            </div>
                            </section>
                            <%-- </s:if> --%>
                        </div>
                    </div></div>


            </section>
        </div>

        <!-- /Highlights -->
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <!-- <script>
        $('input[name="daterange"]').daterangepicker();
        </script>-->
        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GridNavigation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>

        <%-- <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script> --%>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

        <script type="text/javascript">
                                                var deletedRows = [];

                                                $(function () {
                                                    $('#deleteRow').click(function () {
                                                        $('input:checked').each(function () {
                                                            $(this).closest('tr').remove();
                                                        })
                                                    });

                                                });

                                                function getList()
                                                {
                                                    var listName = document.getElementById("listName").value;
                                                    if (listName != -1) {
                                                        document.getElementById("selectedName").value = listName;
                                                    } else {
                                                        document.getElementById("selectedName").value = "";
                                                        //document.getElementById("modifieddate").value = "";
                                                    }
                                                    window.location = "../utilities/codeListSearch.action?listName=" + listName + "&selectedName=" + document.getElementById("selectedName").value;

                                                }




                                                // selecting all records
                                                function selectAllRecords() {
                                                    var rowCount = $('#results tr').length;
                                                    if (document.getElementById("checkboxAll").checked)
                                                    {
                                                        for (var i = 1; i < (rowCount + deletedRows.length); i++) {
                                                            var flag1 = checkArray(i);
                                                            if (flag1 == true)
                                                            {
                                                                continue;
                                                            }
                                                            else if (flag1 == false) {
                                                                document.getElementById('check' + i).checked = true;
                                                            }
                                                        }
                                                    } else {
                                                        for (var i = 1; i < (rowCount + deletedRows.length); i++) {
                                                            var flag1 = checkArray(i);
                                                            if (flag1 == true)
                                                            {
                                                                continue;
                                                            }
                                                            else if (flag1 == false) {
                                                                document.getElementById('check' + i).checked = false;
                                                            }
                                                        }
                                                    }

                                                }

                                                function getUpdateRow() {
                                                    if (document.getElementById("items").value == 0)
                                                    {
                                                        alert("Cannot updae code list when no code list is selected");
                                                    }
                                                    else {
                                                        listName = document.getElementById('listName1').value.trim();
                                                        var checkedCount = 0;
                                                        var ips = {"jsonData": []};
                                                        var rowCount = $('#results tr').length;
                                                        var listName;
                                                        for (i = 1; i < (rowCount + deletedRows.length); i++) {
                                                            var flag1 = checkArray(i);
                                                            if (flag1 == true)
                                                            {
                                                                continue;
                                                            }
                                                            else if (flag1 == false) {
                                                                if (document.getElementById('check' + i).checked) {
                                                                    checkedCount++;
                                                                    if (i <= document.getElementById("items").value) {
//                                                                    listName = document.getElementById('listName' + i).value;
//                                                                }
//                                                                else
//                                                                {
                                                                        // alert('listName----'+listName);
                                                                        
//                                                                    if (listName == "")
//                                                                    {
//                                                                        document.getElementById("messagediv").innerHTML = "<font class='text-danger'>Please Enter New List Name</font>";
//                                                                        return false;
//                                                                    }
                                                                        document.getElementById('senderId' + i).value = "";
                                                                        document.getElementById('recId' + i).value = "";
                                                                        document.getElementById('senderId' + i).value = "";
                                                                        document.getElementById('listVersion' + i).value = 1;
                                                                    }
                                                                    if (document.getElementById('senderItem' + i).value.trim() == "")
                                                                    {
                                                                        alert("Please enter sender code");
                                                                        return false;
                                                                    }
                                                                    if (document.getElementById('recItem' + i).value.trim() == "")
                                                                    {
                                                                        alert("please enter receiver code");
                                                                        return false;
                                                                    }
                                                                    if (document.getElementById('desc' + i).value.trim() == "")
                                                                    {
                                                                        alert("please enter description");
                                                                        return false;
                                                                    }
                                                                    ips["jsonData"].push({
                                                                        "listName1": listName,
                                                                        "senderIdInst": document.getElementById('senderId' + i).value.trim(),
                                                                        "recId": document.getElementById('recId' + i).value.trim(),
                                                                        "listVerson": document.getElementById('listVersion' + i).value.trim(),
                                                                        "senderItem": document.getElementById('senderItem' + i).value.trim(),
                                                                        "recItem": document.getElementById('recItem' + i).value.trim(),
                                                                        "text1": document.getElementById('text1' + i).value.trim(),
                                                                        "text2": document.getElementById('text2' + i).value.trim(),
                                                                        "text3": document.getElementById('text3' + i).value.trim(),
                                                                        "text4": document.getElementById('text4' + i).value.trim(),
                                                                        "desc": document.getElementById('desc' + i).value.trim(),
                                                                        "text5": document.getElementById('text5' + i).value.trim(),
                                                                        "text6": document.getElementById('text6' + i).value.trim(),
                                                                        "text7": document.getElementById('text7' + i).value.trim(),
                                                                        "text8": document.getElementById('text8' + i).value.trim(),
                                                                        "text9": document.getElementById('text9' + i).value.trim()
                                                                    });

                                                                }
                                                            }
                                                        }


                                                        if (checkedCount == 0)
                                                        {
                                                            alert("please select rows to update");
                                                            return false;
                                                        }
                                                        var array = JSON.stringify(ips["jsonData"]);
                                                        document.getElementById('json').value = array;
                                                        document.getElementById('loadingAcoountSearch').style.display = "block";
                                                        jQuery.ajax({
                                                            url: "../ajax/codeVersionUpdate.action",
                                                            type: "POST",
                                                            data: {json: array, listName: listName},
                                                            success: function (result) {
                                                                alert(result);
                                                                if (result != "Please Try Again")
                                                            {
                                                                var listName = document.getElementById("listName").value;
                                                                window.location = "../utilities/codeListSearch.action?listName=" + listName + "&selectedName=" + document.getElementById("selectedName").value;
                                                            }
                                                            else {
                                                                document.getElementById('loadingAcoountSearch').style.display = "none";
                                                            }
                                                            }
                                                        });
                                                        //window.location = "../utilities/codeVersionUpdate.action?listName=" + listName + "&json=" + encodeURIComponent(array);
                                                        //  window.location = "../utilities/codeVersionUpdate.action?listName=" + listName;
                                                    }
                                                }
                                                // 
                                                function selectAllUncheck() {
                                                    var rowCount = $('#results tr').length;
                                                    for (var i = 1; i < (rowCount + deletedRows.length); i++) {
                                                        //alert('hiii');
                                                        var flag1 = checkArray(i);
                                                        if (flag1 == true)
                                                        {
                                                            continue;
                                                        }
                                                        else if (flag1 == false) {
                                                            if (document.getElementById('check' + i).checked == false) {
                                                                document.getElementById("checkboxAll").checked = false;
                                                            }
                                                        }
                                                    }

                                                }

                                                function selectAllCheck() {
                                                    var checkdCount1 = 0;
                                                    var rowCount = $('#results tr').length;
                                                    //alert("rowCount - "+rowCount+" deletedRows.length - "+deletedRows.length);
                                                    for (var i = 1; i < (rowCount + deletedRows.length); i++) {

                                                        var flag1 = checkArray(i);
                                                        if (flag1 == true)
                                                        {
                                                            continue;
                                                        }
                                                        else if (flag1 == false) {
                                                            if (document.getElementById('check' + i).checked == true) {
                                                                checkdCount1++;
                                                            }
                                                            if (checkdCount1 == (rowCount -1)) {
                                                                document.getElementById("checkboxAll").checked = true;
                                                            }
                                                        }
                                                    }
                                                }



                                                function checkArray(i)
                                                {
                                                    var count = deletedRows.length;
                                                    for (var j = 0; j < count; j++)
                                                    {
                                                        if (deletedRows[j] === i) {
                                                            return true;
                                                        }
                                                    }
                                                    return false;
                                                }
                                                function insertRows()
                                                {
                                                    /* if (document.getElementById("items").value != 0)
                                                    {
                                                        alert("Cannot add new code list when existing one is selected");
                                                    }
                                                    else
                                                    { */
                                                        var checkedCount = 0;
                                                        var ips = {"jsonData": []};
                                                        var rowCount = $('#results tr').length;
                                                        for (i = 1; i < (rowCount + deletedRows.length); i++) {
                                                            var flag1 = checkArray(i);
                                                            if (flag1 == true)
                                                            {
                                                                continue;
                                                            }
                                                            else if (flag1 == false) {
                                                                if (document.getElementById('check' + i).checked) {
                                                                    var listName = "";
                                                                    listName = document.getElementById("newname").value.trim();
                                                                    if (listName == "") {
                                                                        alert("Please Enter New List Name");
                                                                        return false;
                                                                    }

                                                                    if (document.getElementById('senderItem' + i).value.trim() == "")
                                                                    {
                                                                        alert("Please enter sender code");
                                                                        return false;
                                                                    }
                                                                    if (document.getElementById('recItem' + i).value.trim() == "")
                                                                    {
                                                                        alert("please enter receiver code");
                                                                        return false;
                                                                    }
                                                                    if (document.getElementById('desc' + i).value.trim() == "")
                                                                    {
                                                                        alert("please enter description");
                                                                        return false;
                                                                    }

                                                                    ips["jsonData"].push({
                                                                        "listName1": listName,
                                                                        "senderIdInst": document.getElementById('senderId' + i).value.trim(),
                                                                        "recId": document.getElementById('recId' + i).value.trim(),
                                                                        "listVerson": document.getElementById('listVersion' + i).value.trim(),
                                                                        "senderItem": document.getElementById('senderItem' + i).value.trim(),
                                                                        "recItem": document.getElementById('recItem' + i).value.trim(),
                                                                        "text1": document.getElementById('text1' + i).value.trim(),
                                                                        "text2": document.getElementById('text2' + i).value.trim(),
                                                                        "text3": document.getElementById('text3' + i).value.trim(),
                                                                        "text4": document.getElementById('text4' + i).value.trim(),
                                                                        "desc": document.getElementById('desc' + i).value.trim(),
                                                                        "text5": document.getElementById('text5' + i).value.trim(),
                                                                        "text6": document.getElementById('text6' + i).value.trim(),
                                                                        "text7": document.getElementById('text7' + i).value.trim(),
                                                                        "text8": document.getElementById('text8' + i).value.trim(),
                                                                        "text9": document.getElementById('text9' + i).value.trim()
                                                                    });


                                                                    checkedCount++;
                                                                }
                                                            }

                                                        }

                                                        var array = JSON.stringify(ips["jsonData"]);

                                                        if (checkedCount == 0)
                                                        {
                                                            alert("please select rows to insert");
                                                            return false;
                                                        }
//                                                        var isExists = "";
//                                                        isExists = checkListName();
//                                                        if (isExists != "Failure" && isExists != "") {
                                                        document.getElementById('loadingAcoountSearch').style.display = "block";
                                                        jQuery.ajax({
                                                            url: "../ajax/codeListAdd.action",
                                                            type: "POST",
                                                            data: {json: array, newListName: document.getElementById("newname").value},
                                                            success: function (result) {
                                                                alert(result);
                                                                if ((result != "Inserted successfully") && (result != "Please Try Again"))
                                                                {
                                                                    document.getElementById("newname").value = "";
                                                                    document.getElementById('loadingAcoountSearch').style.display = "none";
                                                                }
                                                                else {
                                                                    window.location = "../utilities/codeListSearch.action?listName=" + document.getElementById("newname").value + "&selectedName=" + document.getElementById("newname").value;

                                                                }

                                                            }
                                                        });
//                                                        /}
//                                                        else {
//                                                            alert("Please try later");
//                                                        }
                                                        //  window.location = "../utilities/codeListAdd.action?json=" + encodeURIComponent(array);
                                                    }

                                                //}
                                                
                                                function deleteRows()
                                                {

                                                    var checkedCount = 0;
                                                    var ips = {"jsonData": []};
                                                    var rowCount = $('#results tr').length;
                                                    var x = rowCount + deletedRows.length;
                                                    for (i = 1; i < x; i++) {
                                                        var flag1 = checkArray(i);
                                                        if (flag1 == true)
                                                        {
                                                            continue;
                                                        }
                                                        else if (flag1 == false) {
                                                            if (document.getElementById('check' + i).checked) {
                                                                var listName = "";
                                                                if (document.getElementById('items').value == 0)
                                                                {
                                                                    deletedRows.push(i);
                                                                    $('#results tr#' + i).remove();

                                                                }
                                                                if (document.getElementById('items').value > 0 && i > document.getElementById('items').value)
                                                                {
                                                                    $('#results tr#' + i).remove();
                                                                    deletedRows.push(i);


                                                                }
//                                                                    listName = document.getElementById("listName" + i).value;
//                                                                    if (listName == "") {
//                                                                        document.getElementById("messagediv").innerHTML = "<font class='text-danger'>Please select rows with data  to delete</font>";
//                                                                        return false;
//                                                                    }
                                                                if (i <= document.getElementById('items').value) {
                                                                    ips["jsonData"].push({
                                                                        "listName1": document.getElementById("listName1").value,
                                                                        "senderIdInst": document.getElementById('senderId' + i).value,
                                                                        "recId": document.getElementById('recId' + i).value,
                                                                        "listVerson": document.getElementById('listVersion' + i).value,
                                                                        "senderItem": document.getElementById('senderItem' + i).value,
                                                                        "recItem": document.getElementById('recItem' + i).value,
                                                                        "text1": document.getElementById('text1' + i).value,
                                                                        "text2": document.getElementById('text2' + i).value,
                                                                        "text3": document.getElementById('text3' + i).value,
                                                                        "text4": document.getElementById('text4' + i).value,
                                                                        "desc": document.getElementById('desc' + i).value,
                                                                        "text5": document.getElementById('text5' + i).value,
                                                                        "text6": document.getElementById('text6' + i).value,
                                                                        "text7": document.getElementById('text7' + i).value,
                                                                        "text8": document.getElementById('text8' + i).value,
                                                                        "text9": document.getElementById('text9' + i).value
                                                                    });
                                                                }
                                                                checkedCount++;
                                                            }
                                                        }

                                                    }
                                                    if ($('#results tr').length == 1)
                                                    {
                                                        $("#checkboxAll").attr("checked", false);
                                                    }
                                                    if (checkedCount == 0)
                                                    {
                                                        alert("please select rows to delete");
                                                        return false;
                                                    }
                                                    if (document.getElementById('items').value == 0 || ($('#results tr').length - 1 < document.getElementById('items').value))
                                                    {
                                                        return false;
                                                    }
                                                    var rowCount1 = $('#results tr').length;
                                                    var count = 0;
                                                    for (i = 1; i < rowCount1; i++) {
                                                        var flag1 = checkArray(i);
                                                        if (flag1 == true)
                                                        {
                                                            continue;
                                                        }
                                                        else if (flag1 == false) {
                                                            if (document.getElementById('check' + i).checked) {
                                                                count++;
                                                            }
                                                        }
                                                    }
                                                    if (count == 0)
                                                    {
                                                        return false;
                                                    }

                                                    var array = JSON.stringify(ips["jsonData"]);
                                                    document.getElementById('loadingAcoountSearch').style.display = "block";
                                                    jQuery.ajax({
                                                        url: "../ajax/codeListDelete.action",
                                                        type: "POST",
                                                        data: {json: array, listName: document.getElementById('listName').value, selectedName: document.getElementById('selectedName').value},
                                                        success: function (result) {
                                                            alert(result);
                                                            if (result != "Please Try Again")
                                                            {
                                                                var listName = document.getElementById("listName").value;
                                                                if (listName != -1) {
                                                                    document.getElementById("selectedName").value = listName;
                                                                } else {
                                                                    document.getElementById("selectedName").value = "";
                                                                    //document.getElementById("modifieddate").value = "";
                                                                }
                                                                window.location = "../utilities/codeListSearch.action?listName=" + listName + "&selectedName=" + document.getElementById("selectedName").value;
                                                            }
                                                            else {
                                                                document.getElementById('loadingAcoountSearch').style.display = "none";
                                                            }

                                                        }
                                                    });
                                                    //location.reload();
                                                    // window.location = "../utilities/codeListDelete.action?json=" + encodeURIComponent(array) + "&listName=" + document.getElementById('listName').value + "&selectedName=" + document.getElementById('selectedName').value;
                                                    //window.location = "..utilities/codeListSearch.action?listName=" + document.getElementById('listName').value + "&selectedName=" +document.getElementById('selectedName').value;
                                                }
                                                function checkListName() {
                                                    var res = "";
                                                    document.getElementById("loadingAcoountSearch").style.display = "block";
                                                    var newCodeListName = document.getElementById("newname").value;
                                                    jQuery.ajax({
                                                        url: "../ajax/checkCodeListName.action",
                                                        type: "POST",
                                                        data: {newListName: newCodeListName},
                                                        success: function (result) {
                                                            if (result == "Failure")
                                                            {
                                                                res = result;
                                                                document.getElementById("newname").value = "";
                                                                alert("Code List name already exists. Please try with different one");
                                                                document.getElementById('loadingAcoountSearch').style.display = "none";
                                                                return false;
                                                            } else {
                                                                res = result;
                                                            }
                                                            document.getElementById('loadingAcoountSearch').style.display = "none";
                                                        }
                                                    });
                                                    return res;
                                                }


        </script> 
    </body>
</html>
