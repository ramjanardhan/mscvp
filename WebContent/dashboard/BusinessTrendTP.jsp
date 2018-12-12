<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <script>
            function doOnLoad() {
                $("#dashboardLM").addClass("active");
                $('#ltBusinessTrends').addClass('active');
                $('#ltBusinessTrendsTP').addClass('active');
                $('#ltBusinessTrendsTP').css('font-weight', 'bold');
                $('#ltBusinessTrendsTP i').addClass('text-red');
                changeYearMoth();
            }
        </script>
        <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript"  src='<s:url value="../includes/js/highCharts.js"/>'></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    </head>
    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini">   
        <div> <s:include value="../includes/template/header.jsp"/> </div>
        <div> <s:include value="../includes/template/sidemenu.jsp"/> </div>
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <!-- Main content -->
            <section class="content">
                <div class="box-body box box-primary">
                    <div id="text">
                        <span id="resultMessage"></span>
                        <div style="alignment-adjust:central;">
                            <s:form action="../dashboard/tpBusinessSearchTrends.action" method="post" name="documentForm" id="documentForm" theme="simple" onsubmit="return doEdiBusinessTpTrends();">
                                <s:hidden name="tpBusinessYearTransTrends" id="tpBusinessYearTransTrends" value="%{tpBusinessYearTransTrends}"/>
                                <s:hidden name="tpBusinessMonthTransTrends" id="tpBusinessMonthTransTrends" value="%{tpBusinessMonthTransTrends}"/>
                                <s:hidden name="yearOfMonthlyVolumeXAxisValues" id="yearOfMonthlyVolumeXAxisValues" value="%{yearOfMonthlyVolumeXAxisValues}"/>
                                <s:hidden name="monthlyOfDaysVolumeXAxisValues" id="monthlyOfDaysVolumeXAxisValues" value="%{monthlyOfDaysVolumeXAxisValues}"/>
                                <%
                                    int userId = (Integer) session.getAttribute(AppConstants.SES_USER_ID);
                                    Map usrFlowMap = (Map) session.getAttribute(AppConstants.SES_USER_FLOW_MAP);
                                %>
                                <div class="form-group">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="row">
                                                <div class="col-sm-3">
                                                    <s:radio cssClass="myRadio" id="yearmonth" name="yearmonth" value="%{yearmonth}" list="#@java.util.LinkedHashMap@{'Yearly':'Yearly','Monthly':'Monthly'}" onchange="changeYearMoth();"/>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div  class="col-sm-3">
                                                    <label>Business Flow <font color="red">*</font></label>
                                                        <% if ((usrFlowMap.containsValue("Logistics")) && (usrFlowMap.containsValue("Manufacturing"))) {%>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Logistics','Manufacturing'}" name="businessFlows" id="businessFlows" value="%{businessFlows}" onchange="getPartnerAndDocType()" tabindex="2"/>
                                                        <% } else if (usrFlowMap.containsValue("Manufacturing")) {%>
                                                        <s:select cssClass="form-control" list="{'Manufacturing'}" name="businessFlows" id="businessFlows" value="%{businessFlows}" tabindex="2"/>
                                                        <%} else {%>
                                                        <s:select cssClass="form-control" list="{'Logistics'}" name="businessFlows" id="businessFlows" value="%{businessFlows}" tabindex="2"/>
                                                        <%}%>
                                                </div>
                                                <div  class="col-sm-3">
                                                    <label>Trading Partner <font color="red">*</font></label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="partnerMap" name="tpPartnerId" id="tpPartnerId" value="%{tpPartnerId}" tabindex="2"/>
                                                </div>
                                                <div  class="col-sm-3">
                                                    <label>Direction <font color="red">*</font></label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Inbound','Outbound'}" name="direction" id="direction" value="%{direction}" tabindex="2"/>
                                                </div>
                                            </div>
                                           
                                            <div class="row">
                                                <div class="col-sm-3">
                                                    <label>Document Type <font color="red">*</font></label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="2"/>
                                                </div>
                                                <div id="fyear" class="col-sm-3">
                                                    <label>Year <font color="red">*</font></label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="yearList" name="fromYear" id="fromYear" value="%{fromYear}" tabindex="2" />
                                                </div>
                                                <div id="fmonth" class="col-sm-3" style="display: none">
                                                    <label>Month <font color="red">*</font></label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'01':'January','02':'February','03':'March','04':'April','05':'May','06':'June','07':'July','08':'August','09':'September','10':'October','11':'November','12':'December'}" name="fromMonth" id="fromMonth" value="%{fromMonth}" tabindex="2" />
                                                </div>
                                            </div>
                                           
                                            <div class="row">
                                                <div class="col-sm-2"><s:submit value="Search"  cssClass="btn btn-primary col-sm-12" tabindex="15"/></div>
                                                <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="16" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>
                                            </div>
                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div>
                        <%--  out.print("contextPath-->"+contextPath); --%>
                </div>
            </section>
            <section class="content">
                <div class="row" style="display: none" id="result">
                    <div class="col-sm-9">
                        <div id="container" style="min-width: 310px; height: 250px; margin: 0 auto"></div>
                    </div>
                    <div class="col-sm-3" style="display: none" id="result2">
                        <div class="col-sm-12" style="padding-top: 80px"><strong><input type="button" value="Generate PDF" tabindex="17" class="btn btn-primary col-sm-12" onclick="return grid1DashboardDownload('tpdash', 'pdf', document.getElementById('tpBusinessYearTransTrends').value, document.getElementById('yearOfMonthlyVolumeXAxisValues').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"  id="pdf"/></strong></div>
                        <div class="col-sm-12" style="padding-top: 20px"><strong><input type="button" value="Genrate Excel" tabindex="18" class="btn btn-primary col-sm-12" onclick="return grid1DashboardDownload('tpdash', 'xls', document.getElementById('tpBusinessYearTransTrends').value, document.getElementById('yearOfMonthlyVolumeXAxisValues').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"  id="excel"/></strong></div>
                    </div>
                    <div class="col-sm-3" style="display: none" id="result1">
                        <div class="col-sm-12" style="padding-top: 80px"><strong><input type="button" value="Generate PDF" tabindex="17" class="btn btn-primary col-sm-12" onclick="return grid1DashboardDownload('tpdash1', 'pdf', document.getElementById('tpBusinessMonthTransTrends').value, document.getElementById('monthlyOfDaysVolumeXAxisValues').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"  id="pdf"/></strong></div>
                        <div class="col-sm-12" style="padding-top: 20px"><strong><input type="button" value="Genrate Excel" tabindex="18" class="btn btn-primary col-sm-12" onclick="return grid1DashboardDownload('tpdash1', 'xls', document.getElementById('tpBusinessMonthTransTrends').value, document.getElementById('monthlyOfDaysVolumeXAxisValues').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"  id="excel"/></strong></div>
                    </div>
                </div>
                <%-- Side box ends--%>
            </section>
        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <script type="text/javascript">
            var form = document.forms['documentForm'];
            var radios = form.elements["yearmonth"];
            var db = null;
            for (var i = 0; i < radios.length; i++) {
                if (radios[i].checked == true) {
                    db = radios[i].value;
                }
            }
            //Year Result
            var yearofXaxis = document.getElementById('yearOfMonthlyVolumeXAxisValues').value;
            var xAxisofYear = yearofXaxis.split(',');
            var yearofTpResult = document.getElementById('tpBusinessYearTransTrends').value;
            //Month result
            var MonthofXaxis = document.getElementById('monthlyOfDaysVolumeXAxisValues').value;
            var xAxisofMonth = MonthofXaxis.split(',');
            var MonthOfTpResult = document.getElementById('tpBusinessMonthTransTrends').value;
            if (db == 'Yearly' && yearofTpResult != "") {
                document.getElementById('result').style.display = "block";
                document.getElementById('result2').style.display = "block";
                Highcharts.chart('container', {
                    chart: {
                        type: 'spline'
                    },
                    title: {
                        text: 'Trading Partner Trends Business View'
                                //  text: 'EDI/Rail Inbound/Outbound'
                    },
                    subtitle: {
                        text: ''
                    },
                    xAxis: {
                        type: 'datetime',
                        categories: xAxisofYear,
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                        minorGridLineWidth: 0,
                        gridLineWidth: 00,
                        alternateGridColor: null,
                        plotBands: [{
                                from: 0,
                                to: 10000,
                                color: 'rgba(68, 170, 213, 0.1)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }, {// Light breeze
                                from: 10001,
                                to: 20000,
                                color: 'rgba(0, 0, 0, 0)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }, {// Gentle breeze
                                from: 20001,
                                to: 30000,
                                color: 'rgba(68, 170, 213, 0.1)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }]
                    },
                    tooltip: {
                        valueSuffix: ''
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 4,
                            states: {
                                hover: {
                                    lineWidth: 5
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            //   pointInterval: 24 * 3600 * 1000, // one hour
                            //  pointStart: Date.UTC(2017, 8, 20, 0, 0, 0)
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: [{
                            name: 'Values',
                            //  data: [10000, 20000, 1000, 30000,10000, 20000, 1000, 30000,10000, 20000,10000, 20000]
                            data: JSON.parse(yearofTpResult)

                        }],
                    navigation: {
                        menuItemStyle: {
                            fontSize: '10px'
                        }
                    }
                });
            }

            if (db == 'Monthly' && MonthOfTpResult != "") {
                document.getElementById('result').style.display = "block";
                document.getElementById('result1').style.display = "block";
                Highcharts.chart('container', {
                    chart: {
                        type: 'spline'
                    },
                    title: {
                        text: 'Trading Partner Trends Business View'
                                //  text: 'EDI/Rail Inbound/Outbound'
                    },
                    subtitle: {
                        text: ''
                    },
                    xAxis: {
                        type: 'datetime',
                        categories: xAxisofMonth,
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                        minorGridLineWidth: 0,
                        gridLineWidth: 00,
                        alternateGridColor: null,
                        plotBands: [{
                                from: 0,
                                to: 10000,
                                color: 'rgba(68, 170, 213, 0.1)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }, {// Light breeze
                                from: 10001,
                                to: 20000,
                                color: 'rgba(0, 0, 0, 0)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }, {// Gentle breeze
                                from: 20001,
                                to: 30000,
                                color: 'rgba(68, 170, 213, 0.1)',
                                label: {
                                    text: '',
                                    style: {
                                        color: '#606060'
                                    }
                                }
                            }]
                    },
                    tooltip: {
                        valueSuffix: ''
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 4,
                            states: {
                                hover: {
                                    lineWidth: 5
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            //   pointInterval: 24 * 3600 * 1000, // one hour
                            //  pointStart: Date.UTC(2017, 8, 20, 0, 0, 0)
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: [{
                            name: 'Values',
                            //  data: [10000, 20000, 1000, 30000,10000, 20000, 1000, 30000,10000, 20000,10000, 20000]
                            data: JSON.parse(MonthOfTpResult)

                        }],
                    navigation: {
                        menuItemStyle: {
                            fontSize: '10px'
                        }
                    }
                });
            }
            function changeYearMoth() {
                //alert("changeYearMoth");
                var form = document.forms['documentForm'];
                var radios = form.elements["yearmonth"];
                var db = null;
                for (var i = 0; i < radios.length; i++) {
                    if (radios[i].checked == true) {
                        db = radios[i].value;
                    }
                }
                if (db == 'Monthly') {
                    $('#fmonth').show();
                    // $('#fyear').show();
                }
                if (db == 'Yearly') {
                    $('#fmonth').hide();
                    // $('#fyear').show();
                }
            }
        </script>

        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>

        <script type="text/javascript">
            function resetvalues() {
                document.getElementById('fromYear').value = "-1";
                document.getElementById('docType').value = "-1";
                document.getElementById('direction').value = "-1";
                document.getElementById('tpPartnerId').value = "-1";
                document.getElementById('fromMonth').value = "-1";
                document.getElementById('result').style.display = "none";
            }

            function doEdiBusinessTpTrends() {
                // alert('doEdiBusinessTpTrends');
                var form = document.forms['documentForm'];
                var radios = form.elements["yearmonth"];
                var db = null;
                for (var i = 0; i < radios.length; i++) {
                    if (radios[i].checked == true) {
                        db = radios[i].value;
                    }
                }
                var tpPartner = document.getElementById("tpPartnerId").value;
                var edidirection = document.getElementById("direction").value;
                var edidocType = document.getElementById("docType").value;
                var edifromYear = document.getElementById("fromYear").value;
                if (tpPartner == "-1" || tpPartner == null) {
                    document.getElementById("resultMessage").innerHTML = "<font color='red'>Please fill The TpPartner !!</font>";
                    return false;
                }
                if (edidirection == "-1" || edidirection == null) {
                    document.getElementById("resultMessage").innerHTML = "<font color='red'>Please fill The Direction !!</font>";
                    return false;
                }
                if (edidocType == "-1" || edidocType == null) {
                    document.getElementById("resultMessage").innerHTML = "<font color='red'>Please fill The DocType !!</font>";
                    return false;
                }
                if (edifromYear == "-1" || edifromYear == null) {
                    document.getElementById("resultMessage").innerHTML = "<font color='red'>Please fill The Year !!</font>";
                    return false;
                }
                if (db == 'Monthly') {
                    var edifromMonth = document.getElementById("fromMonth").value;
                    if (edifromMonth == "-1" || edifromMonth == null) {
                        document.getElementById("resultMessage").innerHTML = "<font color='red'>Please fill The Month !!</font>";
                        return false;
                    }
                } else {
                    return true;
                }
            }
        </script>

    </body>

</html>