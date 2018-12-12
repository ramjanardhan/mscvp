<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script type="text/javascript"  src='<s:url value="../includes/js/highCharts.js"/>'></script>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript">
            function doOnLoad() {
                $("#dashboardLM").addClass("active");
                $("#dashboardM").addClass("active");
                $("#MEdiDocument").addClass("active");
                $("#MEdiDocument").css('font-weight', 'bold');
                $("#MEdiDocument i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
                $(".highcharts-background").attr("fill", "#E5E5E5");
            }
        </script>

    </head>
    <body  class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();">
        <div> <s:include value="../includes/template/header.jsp"/> </div>
        <div> <s:include value="../includes/template/sidemenu.jsp"/> </div>
        <div class="content-wrapper col-sm-12">
            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <div class="col-sm-10">
                <div class="col-sm-2 pull-right" style="margin-top:10px;" onclick='myFunction()'><a class="btn btn-info">
                        Refresh <span class="glyphicon glyphicon-refresh"></span>
                    </a></div>
            </div>
            <%
                if (session.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                    String responseString = session.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                    out.println(responseString);
                    session.setAttribute(AppConstants.REQ_RESULT_MSG, null);
                } else {
            %>        
            <s:hidden name="xAxisTimeInterval" id="xAxisTimeInterval" value="%{xAxisTimeInterval}"/>
            <s:hidden name="baseValue" id="baseValue" value="%{baseValue}"/>
            <s:hidden name="ediDocuments850IB" id="ediDocuments850IB" value="%{ediDocuments850IB}"/>
            <s:hidden name="ediDocuments850OB" id="ediDocuments850OB" value="%{ediDocuments850OB}"/>
            <s:hidden name="ediDocuments810IB" id="ediDocuments810IB" value="%{ediDocuments810IB}"/>
            <s:hidden name="ediDocuments810OB" id="ediDocuments810OB" value="%{ediDocuments810OB}"/>
            <s:hidden name="ediDocuments856IB" id="ediDocuments856IB" value="%{ediDocuments856IB}"/>
            <s:hidden name="ediDocuments856OB" id="ediDocuments856OB" value="%{ediDocuments856OB}"/>
            <s:hidden name="ediDocuments855IB" id="ediDocuments855IB" value="%{ediDocuments855IB}"/>
            <s:hidden name="ediDocuments855OB" id="ediDocuments855OB" value="%{ediDocuments855OB}"/>
            <section class="content-header">

                <div class="col-xs-5" style="text-align: center">
                    <section><h3>INBOUND</h3></section>
                </div>
                <div class="col-xs-5" style="text-align: center">
                    <section><h3>OUTBOUND</h3></section>

                </div>
            </section>
            <section class="container-fluid">
                <div class="col-sm-12" style="padding-bottom: 10px">
                    <div class="col-sm-5">
                        <div id="204_LOAD_TENDER_IB" style="height: 215px"></div>
                    </div>
                    <div class="col-sm-5">
                        <div id="204_LOAD_TENDER_OB" style="height: 215px"></div>
                    </div>
                </div>
                <br>
                <div class="col-sm-12" style="padding-bottom: 10px">
                    <div class="col-sm-5">
                        <div id="990_RES_LT_IB" style="height: 215px"></div>
                    </div>
                    <div class="col-sm-5">
                        <div id="990_RES_LT_OB" style="height: 215px"></div>
                    </div>
                </div>
                <br>
                <div class="col-sm-12" style="padding-bottom: 10px">
                    <div class="col-sm-5">
                        <div id="214_SHIPMENT_IB" style="height: 215px"></div>
                    </div>
                    <div class="col-sm-5">
                        <div id="214_SHIPMENT_OB" style="height: 215px"></div>
                    </div>
                </div>
                <br>
                <div class="col-sm-12" style="padding-bottom: 10px">
                    <div class="col-sm-5">
                        <div id="210_INVOICE_IB" style="height: 215px"></div>
                    </div>
                    <div class="col-sm-5">
                        <div id="210_INVOICE_OB" style="height: 215px"></div>
                    </div>
                </div>
                <br>
            </section>
            <%    }
            %>
        </div>
        <%--    <div id="footer">  --%>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div> 
        <%--   	</div> --%>
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script type="text/javascript">
                    function myFunction() {
                        location.reload();
                    }
                    //204 response IB
                    // var data = [12000, 100000, 8000, 5000, 60000, 15000, 800, 5000, 80000];
                    //var data1 = ['03:00', '03:15', '03:30', '03:45', '04:00', '04:15', '04:30', '04:45', '05:00'];

                    var xAxisTimeInterval = document.getElementById('xAxisTimeInterval').value;
                  //  alert(xAxisTimeInterval)
                    var xAxisTime = xAxisTimeInterval.split(',');
                    // alert(xAxisTime);
                    var xAxisLabel = 'Last Two Hours';
                    var yAxisLabel = 'Transactional Volume';

                    var baseValue = document.getElementById('baseValue').value;

                    var ediDocuments204ib = document.getElementById('ediDocuments850IB').value;
                   // alert(ediDocuments204ib);
                    var ediDocuments204ibHeight = Math.max.apply(Math, JSON.parse(ediDocuments204ib));

                    var ediDocuments204ob = document.getElementById('ediDocuments850OB').value;
                    var ediDocuments204obHeight = Math.max.apply(Math, JSON.parse(ediDocuments204ob));

                    var ediDocuments990ib = document.getElementById('ediDocuments810IB').value;
                    var ediDocuments990ibHeight = Math.max.apply(Math, JSON.parse(ediDocuments990ib));

                    var ediDocuments990ob = document.getElementById('ediDocuments810OB').value;
                    var ediDocuments990obHeight = Math.max.apply(Math, JSON.parse(ediDocuments990ob));

                    var ediDocuments214ib = document.getElementById('ediDocuments856IB').value;
                    var ediDocuments214ibHeight = Math.max.apply(Math, JSON.parse(ediDocuments214ib));

                    var ediDocuments214ob = document.getElementById('ediDocuments856OB').value;
                    var ediDocuments214obHeight = Math.max.apply(Math, JSON.parse(ediDocuments214ob));

                    var ediDocuments210ib = document.getElementById('ediDocuments855IB').value;
                    var ediDocuments210ibHeight = Math.max.apply(Math, JSON.parse(ediDocuments210ib));

                    var ediDocuments210ob = document.getElementById('ediDocuments855OB').value;
                    var ediDocuments210obHeight = Math.max.apply(Math, JSON.parse(ediDocuments210ob));

                    Highcharts.chart('204_LOAD_TENDER_IB', {
                        //alert();
                        title: {
                            text: '<b>850-Purchase Order</b>',
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments204ibHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments204ib),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

//204 response OB
                    Highcharts.chart('204_LOAD_TENDER_OB', {
                        title: {
                            text: '<b>850-Purchase Order</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments204obHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments204ob),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

                    // 990 response IB
                    Highcharts.chart('990_RES_LT_IB', {
                        title: {
                            text: '<b>810-Invoice</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments990ibHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments990ib),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

                    // 990 response ob
                    Highcharts.chart('990_RES_LT_OB', {
                        title: {
                            text: '<b>810-Invoice</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments990obHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments990ob),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });
                    // 214 shipment IB
                    Highcharts.chart('214_SHIPMENT_IB', {
                        title: {
                            text: '<b>856-Shipment Status</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments214ibHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments214ib),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

                    // 214 shipment ob
                    Highcharts.chart('214_SHIPMENT_OB', {
                        title: {
                            text: '<b>856-Shipment Status</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments214obHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments214ob),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });
                    // 210 invoice IB
                    Highcharts.chart('210_INVOICE_IB', {
                        title: {
                            text: '<b>855-Purchase Order</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments210ibHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments210ib),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

                    // 210 invoice ob
                    Highcharts.chart('210_INVOICE_OB', {
                        title: {
                            text: '<b>855-Purchase Order</b>'
                        },
                        chart: {
                            marginRight: 80 // like left
                        },
                        tooltip: {
                            pointFormat: ' {point.y} '
                        },
                        xAxis: {
                            categories: xAxisTime,
                            title: {
                                text: xAxisLabel
                            }
                        },
                        yAxis: [{
                                lineWidth: 1,
                                max: ediDocuments210obHeight,
                                min: 0,
                                title: {
                                    text: yAxisLabel
                                },
                                plotLines: [{
                                        color: 'blue', // Color value
                                        dashStyle: 'longdashdot', // Style of the plot line. Default to solid
                                        value: baseValue, // Value of where the line will appear
                                        width: 1 // Width of the line    
                                    }]
                            }],
                        series: [{
                                showInLegend: false,
                                data: JSON.parse(ediDocuments210ob),
                                zones: [{
                                        value: baseValue,
                                        color: '#FF0000'
                                    }, {
                                        color: '#90ed7d'
                                    }]
                            }]
                    });

                    // the button action
//            $('#button').click(function () {
//                chart.xAxis[0].setCategories(['J', 'F', 'M', 'A', 'M', 'J', 'J', 'A', 'S', 'O', 'N', 'D']);
//            });
        </script>

    </body>
</html>