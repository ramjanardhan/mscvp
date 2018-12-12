/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function getXMLHttpRequest() {
    var xmlHttpReq = false;
    // to create XMLHttpRequest object in non-Microsoft browsers
    if (window.XMLHttpRequest) {
        xmlHttpReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            // to create XMLHttpRequest object in later versions
            // of Internet Explorer
            xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (exp1) {
            try {
                // to create XMLHttpRequest object in older versions
                // of Internet Explorer
                xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (exp2) {
                xmlHttpReq = false;
            }
        }
    }
    return xmlHttpReq;
}

/**
 * TP method
 */
function readyStateHandlerText1(req, responseTextHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                //                   (document.getElementById("loadingImage")).style.display = "none";
                responseTextHandler(req.responseXML);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        } else {

            // (document.getElementById("loadingImage")).style.display = "block";
        }
    }
}



function readyStateHandlerText(req, responseTextHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                responseTextHandler(req.responseXML);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        }

    }
}



function readyStateHandlerLoadText(req, responseTextHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {

                (document.getElementById("loadingImage")).style.display = "none";
                responseTextHandler(req.responseText);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        } else {
            (document.getElementById("loadingImage")).style.display = "block";
        }
    }
}
function readyStateHandlerLoadText2(req, responseTextHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                (document.getElementById("loadingAcoountSearch")).style.display = "none";
                responseTextHandler(req.responseText);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        } else {

            (document.getElementById("loadingAcoountSearch")).style.display = "block";
        }
    }
}

function getPoDetails(number, number1, db) {
    var num = number;
    var num1 = number1;
    $(function () {
        $('#detail_box').show();
        return false;
    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populatePoDetails);
    var url = "../ajax/getPoDetails.action?poNumber=" + num + "&poInst=" + num1 + "&database=" + db;
    req.open("POST", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function populatePoDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    //   alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('InvErrormessage').innerHTML = "";
        var detail = details.childNodes[0];
        var fileID = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var poNUM = detail.getElementsByTagName("PONUM")[0].childNodes[0].nodeValue;
        var poDATE = detail.getElementsByTagName("PODATE")[0].childNodes[0].nodeValue;
        var poValue = detail.getElementsByTagName("POVALUE")[0].childNodes[0].nodeValue;
        //new adding
        var SHIP_DATE = detail.getElementsByTagName("SHIP_DATE")[0].childNodes[0].nodeValue;
        // var ISA_CONTROL_NUMBER = detail.getElementsByTagName("ISA_CONTROL_NUMBER")[0].childNodes[0].nodeValue; 
        var ROUTINGS = detail.getElementsByTagName("ROUTINGS")[0].childNodes[0].nodeValue;
        var INVOICED_AMOUNT = detail.getElementsByTagName("INVOICED_AMOUNT")[0].childNodes[0].nodeValue;
        var PAYMENT_RECEIVED = detail.getElementsByTagName("PAYMENT_RECEIVED")[0].childNodes[0].nodeValue;
        var SHIP_ADDRESS_ID = detail.getElementsByTagName("SHIP_ADDRESS_ID")[0].childNodes[0].nodeValue;
        var BILL_ADDRESS_ID = detail.getElementsByTagName("BILL_ADDRESS_ID")[0].childNodes[0].nodeValue;

        var sapIdocNum = detail.getElementsByTagName("SAPIDOCNUM")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;

        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var deilvaryName = detail.getElementsByTagName("DELSTATUS")[0].childNodes[0].nodeValue;
        var itemQty = detail.getElementsByTagName("ITEMQTY")[0].childNodes[0].nodeValue;
        var sonumber = detail.getElementsByTagName("SO_NUMBER")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;

        var ORDER_STATUS = detail.getElementsByTagName("ORDER_STATUS")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;

        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;

        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var FILE_TYPE = detail.getElementsByTagName("FILE_TYPE")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;


        // Sap Detals

        var SAP_DETAILS = detail.getElementsByTagName("SAP_DETAILS")[0].childNodes[0].nodeValue;
        var SAP_USER;
        var IDOC_NUMBER;
        var PO_NUMBER;
        var PO_DATE;
        var IDOC_STATUS_CODE;
        var IDOC_STATUS_DESCRIPTION;
        if (SAP_DETAILS != 'NO') {

            SAP_USER = detail.getElementsByTagName("SAP_USER")[0].childNodes[0].nodeValue;
            IDOC_NUMBER = detail.getElementsByTagName("IDOC_NUMBER")[0].childNodes[0].nodeValue;
            PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
            PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_CODE = detail.getElementsByTagName("IDOC_STATUS_CODE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_DESCRIPTION = detail.getElementsByTagName("IDOC_STATUS_DESCRIPTION")[0].childNodes[0].nodeValue;

        }

        if (fileID != "NO") {
            document.getElementById('POInstanceId').value = fileID;
        } else {
            document.getElementById('POInstanceId').value = "--";
        }
        //document.getElementById('POQty').value = itemQty;
        if (poNUM != "NO") {
            document.getElementById('PONum').value = poNUM;
        } else {
            document.getElementById('PONum').value = "--";
        }

        if (poDATE != "NO") {
            document.getElementById('PODates').value = poDATE;
        } else {
            document.getElementById('PODates').value = "--";
        }
        /*
         if (poValue != "NO") {
         document.getElementById('POValue').value = poValue;
         }
         else {
         document.getElementById('POValue').value = "--";
         }
         if (SHIP_DATE != "NO") {
         document.getElementById('POShipDate').value = SHIP_DATE;
         }
         else {
         document.getElementById('POShipDate').value = "--";
         }
         if (ROUTINGS != "NO") {
         document.getElementById('PORoutings').value = ROUTINGS;
         }
         else
         {
         document.getElementById('PORoutings').value = "--";
         }
         if (INVOICED_AMOUNT != "NO") {
         document.getElementById('INVOICED_AMOUNT').value = INVOICED_AMOUNT;
         }
         else
         {
         document.getElementById('INVOICED_AMOUNT').value = "--";
         
         }
         if (PAYMENT_RECEIVED != "NO") {
         document.getElementById('POPaymentReceived').value = PAYMENT_RECEIVED;
         }
         else {
         document.getElementById('POPaymentReceived').value = "--";
         }
         if (SHIP_ADDRESS_ID != "NO") {
         document.getElementById('POShipAddrId').value = SHIP_ADDRESS_ID;
         }
         else {
         document.getElementById('POShipAddrId').value = "--";
         }
         if (BILL_ADDRESS_ID != "NO") {
         document.getElementById('POBillAddrId').value = BILL_ADDRESS_ID;
         }
         else
         {
         document.getElementById('POBillAddrId').value = "--";
         }
         if (sonumber != "NO") {
         document.getElementById('POSoNumber').value = sonumber;
         }
         else {
         document.getElementById('POSoNumber').value = "--";
         }
         */

//        if (sapIdocNum != "NO") {
//            document.getElementById('POSapIdocNum').value = sapIdocNum;
//        } else {
//            document.getElementById('POSapIdocNum').value = "--";
//        }
//        if (deilvaryName != "NO") {
//            document.getElementById('PODeilvaryName').value = deilvaryName;
//        }
//        else {
//            document.getElementById('PODeilvaryName').value = "--";
//        }
        if (FILE_TYPE != "NO") {
            document.getElementById('PODocumentType').value = FILE_TYPE;
        } else {
            document.getElementById('PODocumentType').value = "--";
        }
        if (TRANSACTION_TYPE != "NO") {
            document.getElementById('POTransactionType').value = TRANSACTION_TYPE;
        } else {
            document.getElementById('POTransactionType').value = "--";
        }
        if (SENDER_ID != "NO") {
            document.getElementById('POSenderId').value = SENDER_ID;
        } else {
            document.getElementById('POSenderId').value = "--";
        }
        if (SENDER_NAME != "NO") {
            document.getElementById('POSenderName').value = SENDER_NAME;
        } else {
            document.getElementById('POSenderName').value = "--";
        }
        if (RECEIVER_ID != "NO") {
            document.getElementById('POReceiverId').value = RECEIVER_ID;
        } else {
            document.getElementById('POReceiverId').value = "--";
        }
        if (RECEIVER_NAME != "NO") {
            document.getElementById('POReceiverName').value = RECEIVER_NAME;
        } else {
            document.getElementById('POReceiverName').value = "--";
        }
        if (ISA_NUMBER != "NO") {
            document.getElementById('POIsa').value = ISA_NUMBER;
        } else {
            document.getElementById('POIsa').value = "--";
        }
        if (GS_CONTROL_NUMBER != "NO") {
            document.getElementById('POGs').value = GS_CONTROL_NUMBER;
        } else {
            document.getElementById('POGs').value = "--";
        }
        if (ST_CONTROL_NUMBER != "NO") {
            document.getElementById('POSt').value = ST_CONTROL_NUMBER;
        } else {
            document.getElementById('POSt').value = "--";
        }
        if (ISA_DATE != "NO") {
            document.getElementById('POIsADate').value = ISA_DATE;
        } else {
            document.getElementById('POIsADate').value = "--";
        }
        if (ISA_TIME != "NO") {
            document.getElementById('POIsATime').value = ISA_TIME;
        } else {
            document.getElementById('POIsATime').value = "--";
        }
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('POStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('POStatus').value = STATUS;
        } else {
            document.getElementById('POStatus').value = STATUS;
        }
//        if (ORDER_STATUS != "NO") {
//            document.getElementById('POOrderStatus').value = ORDER_STATUS;
//        }
//        else {
//            document.getElementById('POOrderStatus').value = "--";
//        }
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('POPreTransition').innerHTML = "--";
        } else {
            document.getElementById('POPreTransition').innerHTML = "<a style=\"color: deepskyblue\" style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('POPostTransition').innerHTML = "--";

        } else {
            document.getElementById('POPostTransition').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('POAckFileId').innerHTML = "--";

        } else {
            document.getElementById('POAckFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";

        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('InvErrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('InvErrormessage').innerHTML = "--";
        }

        if (SAP_DETAILS != 'NO') {
            document.getElementById('sapDeatails').style.display = "block";
            //document.getElementById('SAP_USER').value = SAP_USER;
            document.getElementById('IDOC_NUMBER').value = IDOC_NUMBER;
            document.getElementById('PO_NUMBER').value = PO_NUMBER;
            //document.getElementById('PO_DATE').value = PO_DATE;
            document.getElementById('IDOC_STATUS_CODE').value = IDOC_STATUS_CODE;
            //document.getElementById('IDOC_STATUS_DESCRIPTION').value = IDOC_STATUS_DESCRIPTION;
        }

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}

/*Reterving ASN Details using ASN Number
 *DATE : 03/21/2013
 * 
 */

function getAsnDetails(number, ponum, fileId, db) {
    var num = number;
    var poNum = ponum;
    $(function () {
        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateASNDetails);

    var url = "../ajax/getASNDetails.action?asnNumber=" + num + "&poNumber=" + poNum + "&fileId=" + fileId + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateASNDetails(responseXML) {

    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('mserrormessage').innerHTML = "";
        var detail = details.childNodes[0];
        var fileID = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var asnNum = detail.getElementsByTagName("ASNNUMBER")[0].childNodes[0].nodeValue;
        var poNum = detail.getElementsByTagName("PONUMBER")[0].childNodes[0].nodeValue;
        var bolNum = detail.getElementsByTagName("BOLNUMBER")[0].childNodes[0].nodeValue;
        var isaNum = detail.getElementsByTagName("ISANUMBER")[0].childNodes[0].nodeValue;
        var isaDate = detail.getElementsByTagName("ISADATE")[0].childNodes[0].nodeValue;
        var isaTime = detail.getElementsByTagName("ISATIME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var ORGFILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        var SHIPDATE = detail.getElementsByTagName("SHIPDATE")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var FILE_TYPE = detail.getElementsByTagName("FILE_TYPE")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;
        // Sap Detals

        var SAP_DETAILS = detail.getElementsByTagName("SAP_DETAILS")[0].childNodes[0].nodeValue;
        var SAP_USER;
        var IDOC_NUMBER;
        var PO_NUMBER;
        var PO_DATE;
        var IDOC_STATUS_CODE;
        var IDOC_STATUS_DESCRIPTION;
        if (SAP_DETAILS != 'NO') {

            SAP_USER = detail.getElementsByTagName("SAP_USER")[0].childNodes[0].nodeValue;
            IDOC_NUMBER = detail.getElementsByTagName("IDOC_NUMBER")[0].childNodes[0].nodeValue;
            PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
            PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_CODE = detail.getElementsByTagName("IDOC_STATUS_CODE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_DESCRIPTION = detail.getElementsByTagName("IDOC_STATUS_DESCRIPTION")[0].childNodes[0].nodeValue;

        }
        if (fileID != "NO") {
            document.getElementById('msfileID').value = fileID;
        } else {
            document.getElementById('msfileID').value = "--";
        }
        if (asnNum != "NO") {
            document.getElementById('msasnNum').value = asnNum;
        } else {
            document.getElementById('msasnNum').value = "--";
        }
        if (poNum != "NO") {
            document.getElementById('mspoNum').value = poNum;
        } else {
            document.getElementById('mspoNum').value = "--";
        }
        if (bolNum != "NO") {
            document.getElementById('msbolNum').value = bolNum;
        } else {
            document.getElementById('msbolNum').value = "--";
        }
        if (SHIPDATE != "NO") {
            document.getElementById('msshipDate').value = SHIPDATE;
        } else {
            document.getElementById('msshipDate').value = "--";
        }
        if (FILE_TYPE != "NO") {
            document.getElementById('msdocType').value = FILE_TYPE;
        } else {
            document.getElementById('msdocType').value = "--";
        }
        if (TRANSACTION_TYPE != "NO") {
            document.getElementById('mstransType').value = TRANSACTION_TYPE;
        } else {
            document.getElementById('mstransType').value = "--";
        }
        if (isaNum != "NO") {
            document.getElementById('msisaNum').value = isaNum;
        } else {
            document.getElementById('msisaNum').value = "--";
        }
        if (SENDER_ID != "NO") {
            document.getElementById('mssenderId').value = SENDER_ID;
        } else {
            document.getElementById('mssenderId').value = "--";
        }
        if (SENDER_NAME != "NO") {
            document.getElementById('mssenderName').value = SENDER_NAME;
        } else {
            document.getElementById('mssenderName').value = "--";
        }
        if (RECEIVER_ID != "NO") {
            document.getElementById('msreceiverId').value = RECEIVER_ID;
        } else {
            document.getElementById('msreceiverId').value = "--";
        }
        if (RECEIVER_NAME != "NO") {
            document.getElementById('msreceiverName').value = RECEIVER_NAME;
        } else {
            document.getElementById('msreceiverName').value = "--";
        }
        if (GS_CONTROL_NUMBER != "NO") {
            document.getElementById('msgsControlNo').value = GS_CONTROL_NUMBER;
        } else {
            document.getElementById('msgsControlNo').value = "--";
        }
        if (ST_CONTROL_NUMBER != "NO") {
            document.getElementById('msstControlNo').value = ST_CONTROL_NUMBER;
        } else {
            document.getElementById('msstControlNo').value = "--";
        }
        if (isaDate != "NO") {
            document.getElementById('msisaDate').value = isaDate;
        } else {
            document.getElementById('msisaDate').value = "--";
        }
        if (isaTime != "NO") {
            document.getElementById('msisaTime').value = isaTime;
        } else {
            document.getElementById('msisaTime').value = "--";
        }
        if (STATUS != "NO") {
            document.getElementById('msstatus').value = STATUS.toUpperCase();
        } else {
            document.getElementById('msstatus').value = "--";
        }
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('mspreTransFilepath').innerHTML = "--";
        } else {
            //<a href=\"../download/getAttachment.action?locationAvailable="+POST_TRANS_FILEPATH+"\">Download</a>
            document.getElementById('mspreTransFilepath').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
            // document.getElementById('mspreTransFilepath').value = PRE_TRANS_FILEPATH;
        }

        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('mspostTransFilepath').innerHTML = "--";
        } else {
            document.getElementById('mspostTransFilepath').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ACKFILEID == "No File") {
            document.getElementById('msackFileId').innerHTML = "--";
        } else {
            document.getElementById('msackFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('mserrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('mserrormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}

/*
 * Reteriving Invoice Table Details
 * Date : 03/21/2013
 * 
 */
function getInvDetails(number, ponum, fileID) {
    var db = document.forms["invoiceForm"]["database"].value;
    var num = number;
    $(function () {
        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateInvDetails);

    var url = "../ajax/getInvDetails.action?invNumber=" + num + "&poNumber=" + ponum + "&fileId=" + fileID + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateInvDetails(responseXML) {

    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('mierrormessage').innerHTML = "";
        var detail = details.childNodes[0];
        var fileID = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var invNum = detail.getElementsByTagName("INVNUMBER")[0].childNodes[0].nodeValue;
        var poNum = detail.getElementsByTagName("PONUMBER")[0].childNodes[0].nodeValue;
        var itemQty = detail.getElementsByTagName("ITEMQTY")[0].childNodes[0].nodeValue;
        var invAmt = detail.getElementsByTagName("INVAMT")[0].childNodes[0].nodeValue;
        var invDate = detail.getElementsByTagName("INVDATE")[0].childNodes[0].nodeValue;
        var isaNum = detail.getElementsByTagName("ISANUM")[0].childNodes[0].nodeValue;
        var isaDate = detail.getElementsByTagName("ISADATE")[0].childNodes[0].nodeValue;
        var isaTime = detail.getElementsByTagName("ISATIME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var ORGFILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var FILETYPE = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;

        // Sap Detals
        var SAP_DETAILS = detail.getElementsByTagName("SAP_DETAILS")[0].childNodes[0].nodeValue;
        var SAP_USER;
        var IDOC_NUMBER;
        var PO_NUMBER;
        var PO_DATE;
        var IDOC_STATUS_CODE;
        var IDOC_STATUS_DESCRIPTION;
        if (SAP_DETAILS != 'NO') {

            SAP_USER = detail.getElementsByTagName("SAP_USER")[0].childNodes[0].nodeValue;
            IDOC_NUMBER = detail.getElementsByTagName("IDOC_NUMBER")[0].childNodes[0].nodeValue;
            PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
            PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_CODE = detail.getElementsByTagName("IDOC_STATUS_CODE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_DESCRIPTION = detail.getElementsByTagName("IDOC_STATUS_DESCRIPTION")[0].childNodes[0].nodeValue;

        }


        document.getElementById('mifileID').value = fileID;
        document.getElementById('miinvNum').value = invNum;

        document.getElementById('mipoNum').value = poNum;
        document.getElementById('miinvAmt').value = invAmt;

        if (invDate != "NO") {
            document.getElementById('miinvDate').value = invDate;
        } else {
            document.getElementById('miinvDate').value = "--";
        }
        document.getElementById('miitemQty').value = itemQty;

        document.getElementById('misenderId').value = SENDER_ID;
        document.getElementById('misenderName').value = SENDER_NAME;

        document.getElementById('mireceiverId').value = RECEIVER_ID;
        document.getElementById('mireceiverName').value = RECEIVER_NAME;

        document.getElementById('miisaNum').value = isaNum;
        document.getElementById('migsControlNo').value = GS_CONTROL_NUMBER;

        document.getElementById('mistControlNo').value = ST_CONTROL_NUMBER;
        document.getElementById('mistatus').value = STATUS.toUpperCase();

        document.getElementById('miisaDate').value = isaDate;
        document.getElementById('miisaTime').value = isaTime;

        document.getElementById('mitransType').value = TRANSACTION_TYPE;
        document.getElementById('mifileType').value = FILETYPE;


//        if (SAP_DETAILS != 'NO') {
//
//            document.getElementById('sapDiv').style.display = "block";
//
//            document.getElementById('misapUser').value = SAP_USER;
//            document.getElementById('miidocNo').value = IDOC_NUMBER;
//
//            document.getElementById('mipoNo').value = PO_NUMBER;
//            document.getElementById('mipoDate').value = PO_DATE;
//
//            document.getElementById('miidocStatusCode').value = IDOC_STATUS_CODE;
//            document.getElementById('miidocStatusDesc').value = IDOC_STATUS_DESCRIPTION;
//        }

        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('mipreTransFilepath').innerHTML = "--";
        } else {
            document.getElementById('mipreTransFilepath').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('mipostTransFilepath').innerHTML = "--";
        } else {
            document.getElementById('mipostTransFilepath').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ACKFILEID == "No File") {
            document.getElementById('miackFileId').innerHTML = "--";
        } else {
            document.getElementById('miackFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('mierrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('mierrormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}

/**
 * For doc Ajax call
 * 
 */
function getDocDetails(number, ponum, id, db) {
    var num = number;
    var ponum = ponum;
    var id = id;
    $(function () {
        $('#detail_box').show();
        return false;
    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateDocDetails);
    var url = "../ajax/getDocDetails.action?isaNumber=" + num + "&poNumber=" + ponum + "&id=" + id + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}



function populateDocDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('errorDiv').value = "";
        var detail = details.childNodes[0];

        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var docType = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;

        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDERID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVERID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var SEC_KEY_VAL = detail.getElementsByTagName("SEC_KEY_VAL")[0].childNodes[0].nodeValue;
        var PRI_KEY_VAL = detail.getElementsByTagName("PRI_KEY_VAL")[0].childNodes[0].nodeValue;
        var PRI_KEY_TYPE = detail.getElementsByTagName("PRI_KEY_TYPE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;
        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;

        // Sap Detals

        var SAP_DETAILS = detail.getElementsByTagName("SAP_DETAILS")[0].childNodes[0].nodeValue;
        var SAP_USER;
        var IDOC_NUMBER;
        var PO_NUMBER;
        var PO_DATE;
        var IDOC_STATUS_CODE;
        var IDOC_STATUS_DESCRIPTION;
        if (SAP_DETAILS != 'NO') {

            SAP_USER = detail.getElementsByTagName("SAP_USER")[0].childNodes[0].nodeValue;
            IDOC_NUMBER = detail.getElementsByTagName("IDOC_NUMBER")[0].childNodes[0].nodeValue;
            PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
            PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_CODE = detail.getElementsByTagName("IDOC_STATUS_CODE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_DESCRIPTION = detail.getElementsByTagName("IDOC_STATUS_DESCRIPTION")[0].childNodes[0].nodeValue;

        }
        document.getElementById('ManFileId').value = fileid;
        document.getElementById('ManPurchaseOrder').value = SEC_KEY_VAL;
        if (PRI_KEY_TYPE != "PO") {
            document.getElementById('prikeytypeandvalue').style.display = "block";
            document.getElementById('Manpri_key_type').value = PRI_KEY_TYPE;
            document.getElementById('Manpri_key_value').value = PRI_KEY_VAL;
        } else {
            document.getElementById('prikeytypeandvalue').style.display = "none";
        }
        if (SENDER_NAME == "NULL") {
            SENDER_NAME = "Null";
        }
        if (RECEIVER_NAME == "NULL") {
            RECEIVER_NAME = "Null";
        }
        document.getElementById('ManDocumentType').value = docType;
        document.getElementById('ManTransactionType').value = TRANSACTION_TYPE;
        document.getElementById('ManSenderId').value = SENDER_ID;
        document.getElementById('ManSenderName').value = SENDER_NAME;
        document.getElementById('ManReceiverId').value = RECEIVER_ID;
        document.getElementById('ManReceiverName').value = RECEIVER_NAME;
        document.getElementById('ManISA').value = ISA_NUMBER;
        document.getElementById('ManGs').value = GS_CONTROL_NUMBER;
        document.getElementById('ManSt').value = ST_CONTROL_NUMBER;
        document.getElementById('ManIsADate').value = ISA_DATE;
        document.getElementById('ManIsATime').value = ISA_TIME;

        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('ManStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('ManStatus').value = STATUS;
        } else {
            document.getElementById('ManStatus').value = STATUS;
        }

        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('ManPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('ManPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('ManPostTranslation').innerHTML = "--";
        } else {
            document.getElementById('ManPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ACKFILEID == "No File") {
            document.getElementById('ManAckFileId').innerHTML = "--";
        } else {
            document.getElementById('ManAckFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('InvErrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('InvErrormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        /*  if (SAP_DETAILS != 'NO') {
         document.getElementById('sapDiv').style.display = "block";
         if (SAP_USER != "" && SAP_USER != null) {
         document.getElementById('SAP_USER').value = SAP_USER;
         } else {
         document.getElementById('SAP_USER').value = "--";
         }
         if (IDOC_NUMBER != "" && IDOC_NUMBER != null) {
         document.getElementById('IDOC_NUMBER').value = IDOC_NUMBER;
         } else {
         document.getElementById('IDOC_NUMBER').value = "--";
         }
         if (PO_NUMBER != "" && PO_NUMBER != null) {
         document.getElementById('PO_NUMBER').value = IDOC_NUMBER;
         } else {
         document.getElementById('PO_NUMBER').value = "--";
         }
         if (PO_DATE != "" && PO_DATE != null) {
         document.getElementById('PO_DATE').value = IDOC_NUMBER;
         } else {
         document.getElementById('PO_DATE').value = "--";
         }
         if (IDOC_STATUS_CODE != "" && IDOC_STATUS_CODE != null) {
         document.getElementById('IDOC_STATUS_CODE').value = IDOC_NUMBER;
         } else {
         document.getElementById('IDOC_STATUS_CODE').value = "--";
         }
         if (IDOC_STATUS_DESCRIPTION != "" && IDOC_STATUS_DESCRIPTION != null) {
         document.getElementById('IDOC_STATUS_DESCRIPTION').value = IDOC_NUMBER;
         } else {
         document.getElementById('IDOC_STATUS_DESCRIPTION').value = "--";
         }
         } */

        document.getElementById('ManNullValues').innerHTML = "<a href=\"javascript:getNullValues('<%=docRepositoryBean.getId()%>');\">Dispalay Null</a></td></tr>";
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";
    }
    $('#hide-menu1').addClass('show-menu');
}



/**
 * Doc copying using Ajax call
 * 
 */
function getProcess(btnValue, list, db) {
    var po_Files = "";

    if ((btnValue.value == 'ReTransmit')) {
        var res;
        var i = 0;
        for (var j = 0; j < list; j++) {
            if (document.getElementById("check_List" + j) != null) {
                if (list == 1) {
                    res = document.getElementById("check_List" + j).checked;
                } else {
                    res = document.getElementById("check_List" + j).checked;
                }
                if (res == true) {
                    i = i + 1;
                    if (i == 1) {
                        po_Files = po_Files + document.getElementById("text" + j).value + "*";
                        po_Files = po_Files + document.getElementById("Instance" + j).value + "@";
                    }
                    if (i != 1)
                    {
                        alert("Please select one checkbox to retransmission for PO !!");
                        return false;
                    }
                }
            }
        }

        // alert("length-->"+po_Files);
        if ((po_Files != "") && (po_Files != null)) {
            var r = confirm("Please confirm retransmission of the selected PO!");
            if (r == true) {
                getcopy(po_Files, "POST", db);
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReTransmit");
        }
    }

    if ((btnValue.value == 'ReSubmit')) {
        var i = 0;
        // alert("ReSubmit-->"+list);
        for (var j = 0; j < list; j++) {
            var res;
            if (document.getElementById("check_List" + j) != null) {
                if (list == 1) {
                    res = document.getElementById("check_List" + j).checked;
                } else {
                    res = document.getElementById("check_List" + j).checked;
                }
                if (res == true) {
                    i = i + 1;
                    if (i == 1) {
                        po_Files = po_Files + document.getElementById("text" + j).value + "*";
                        po_Files = po_Files + document.getElementById("Instance" + j).value + "@";
                    } else
                    {
                        alert("Please select one checkbox to retransmission for PO !!");
                        return false;
                    }
                }
            }
        }

        if ((po_Files != "") && (po_Files != null)) {
            var r = confirm("Please confirm resubmission of the selected PO!");
            if (r == true) {
                getcopy(po_Files, "PRE", db);
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReSubmit");
        }
    }
}

function populateDocCopy(responseText) {
    alert(responseText);
    document.getElementById("purchaseForm").submit();
}


function getcopy(PO_LIST, type, db) {
    //var req = new XMLHttpRequest();
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerString(req, populateDocCopy);
    var url = "../ajax/getDocCopy.action?poList=" + PO_LIST + "&type=" + type + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function readyStateHandlerString(req, responseTextHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                responseTextHandler(req.responseText);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        }
    }
}
/**
 * Shipment copying using Ajax call
 * 
 */

function getProcesAsn(btnValue, list) {
    // alert("hi");
    var asn_Files = "";
    if ((btnValue.value == 'ReTransmit')) {
        // alert("ReTransmit-->"+list);
        var res;
        // alert(document.shipmentForm.check_List[0]);
        for (var j = 0; j < list; j++) {
            if (list == 1) {
                res = document.shipmentForm.check_List.checked;
            } else {
                res = document.shipmentForm.check_List[j].checked;
            }
            //alert("res---->"+res);
            if (res == true) {
                asn_Files = asn_Files + document.getElementById("text" + j).value + "|";
                asn_Files = asn_Files + document.getElementById("Instance" + j).value + "^";
                alert("po_Files--" + asn_Files);
            }
        }
        //alert("length-->"+asn_Files);
        if ((asn_Files != "") && (asn_Files != null)) {
            var r = confirm("Please confirm retransmission of the selected ASN!");
            if (r == true) {
                getAsncopy(asn_Files, "POST");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReTransmit");
        }
    }

    if ((btnValue.value == 'ReSubmit')) {
        // alert("ReSubmit-->"+list);
        for (var j = 0; j < list; j++) {

            var res;
            if (list == 1) {
                res = document.shipmentForm.check_List.checked;
            } else {
                res = document.shipmentForm.check_List[j].checked;
            }
            if (res == true) {
                asn_Files = asn_Files + document.getElementById("text" + j).value + "|";
                asn_Files = asn_Files + document.getElementById("Instance" + j).value + "^";
            }
        }

        // alert("length-->"+asn_Files);
        if ((asn_Files != "") && (asn_Files != null)) {
            var r = confirm("Please confirm resubmission of the selected ASN!");
            if (r == true) {
                getAsncopy(asn_Files, "PRE");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReSubmit");
        }
    }
}

function getAsncopy(ASN_LIST, type) {
    //var req = new XMLHttpRequest();
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerString(req, populateDocAsnCopy);
    var url = "../ajax/getDocASNCopy.action?asnList=" + ASN_LIST + "&type=" + type;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateDocAsnCopy(responseText) {
    alert(responseText);
    document.getElementById("shipmentForm").submit();
}



/**
 * Invices copying using Ajax call
 * 
 */



function getProcesInv(btnValue, list) {
    // alert("hi");
    var inv_Files = "";
    if ((btnValue.value == 'ReTransmit')) {
        // alert("ReTransmit-->"+list);
        var res;
        // alert(document.shipmentForm.check_List[0]);
        for (var j = 0; j < list; j++) {
            if (list == 1) {
                res = document.invoiceForm.check_List.checked;
            } else {
                res = document.invoiceForm.check_List[j].checked;
            }
            //alert("res---->"+res);
            if (res == true) {
                inv_Files = inv_Files + document.getElementById("text" + j).value + "|";
                inv_Files = inv_Files + document.getElementById("Instance" + j).value + "^";
                alert("po_Files--" + inv_Files);
            }
        }
        //alert("length-->"+asn_Files);
        if ((inv_Files != "") && (inv_Files != null)) {
            var r = confirm("Please confirm retransmission of the selected INVOICE!");
            if (r == true) {
                getInvcopy(inv_Files, "POST");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReTransmit");
        }
    }

    if ((btnValue.value == 'ReSubmit')) {
        // alert("ReSubmit-->"+list);
        for (var j = 0; j < list; j++) {

            var res;
            if (list == 1) {
                res = document.invoiceForm.check_List.checked;
            } else {
                res = document.invoiceForm.check_List[j].checked;
            }
            if (res == true) {
                inv_Files = inv_Files + document.getElementById("text" + j).value + "|";
                inv_Files = inv_Files + document.getElementById("Instance" + j).value + "^";
            }
        }

        // alert("length-->"+asn_Files);
        if ((inv_Files != "") && (inv_Files != null)) {
            var r = confirm("Please confirm resubmission of the selected INVOICE!");
            if (r == true) {
                getInvcopy(inv_Files, "PRE");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReSubmit");
        }
    }
}
function getInvcopy(INV_LIST, type) {
    //var req = new XMLHttpRequest();
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerString(req, populateDocInvCopy);
    var url = "../ajax/getInvCopy.action?invList=" + INV_LIST + "&type=" + type;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateDocInvCopy(responseText) {
    alert(responseText);
    document.getElementById("invoiceForm").submit();
}




function getProcesPayment(btnValue, list) {
    // alert("hi");
    var payment_Files = "";
    if ((btnValue.value == 'ReTransmit')) {
        // alert("ReTransmit-->"+list);
        var res;
        // alert(document.shipmentForm.check_List[0]);
        for (var j = 0; j < list; j++) {
            if (list == 1) {
                res = document.paymentForm.check_List.checked;
            } else {
                res = document.paymentForm.check_List[j].checked;
            }
            //alert("res---->"+res);
            if (res == true) {
                payment_Files = payment_Files + document.getElementById("text" + j).value + "|";
                payment_Files = payment_Files + document.getElementById("Instance" + j).value + "^";
                alert("po_Files--" + payment_Files);
            }
        }
        //alert("length-->"+asn_Files);
        if ((payment_Files != "") && (payment_Files != null)) {
            var r = confirm("Please confirm retransmission of the selected PAYMENT!");
            if (r == true) {
                getPaymentcopy(payment_Files, "POST");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReTransmit");
        }
    }

    if ((btnValue.value == 'ReSubmit')) {
        // alert("ReSubmit-->"+list);
        for (var j = 0; j < list; j++) {

            var res;
            if (list == 1) {
                res = document.paymentForm.check_List.checked;
            } else {
                res = document.paymentForm.check_List[j].checked;
            }
            if (res == true) {
                payment_Files = payment_Files + document.getElementById("text" + j).value + "|";
                payment_Files = payment_Files + document.getElementById("Instance" + j).value + "^";
            }
        }

        // alert("length-->"+asn_Files);
        if ((payment_Files != "") && (payment_Files != null)) {
            var r = confirm("Please confirm resubmission of the selected PAYMENT!");
            if (r == true) {
                getPaymentcopy(payment_Files, "PRE");
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReSubmit");
        }
    }
}
function getPaymentcopy(Payment_LIST, type) {
    //var req = new XMLHttpRequest();
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerString(req, populateDocPaymentCopy);
    var url = "../ajax/getPaymentCopy.action?paymentList=" + Payment_LIST + "&type=" + type;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateDocPaymentCopy(responseText) {
    alert(responseText);
    document.getElementById("paymentForm").submit();
}
/**
 * LoadTender copying using Ajax call
 * 
 */
function getloadTenderProcess(btnValue, list, db) {

    var load_Files = "";
    if ((btnValue.value == 'ReTransmit')) {
        //  alert("ReTransmit-->"+list);
        var i = 0;
        var res;
        for (var j = 0; j < list; j++) {
            if (document.getElementById("check_List" + j) != null) {
                if (list == 1) {
                    res = document.getElementById("check_List" + j).checked;
                } else {
                    res = document.getElementById("check_List" + j).checked;
                }
                // alert("res---->"+res);
                if (res == true) {
                    i = i + 1;
                    if (i == 1) {
                        load_Files = load_Files + document.getElementById("text" + j).value + "|";
                        load_Files = load_Files + document.getElementById("Instance" + j).value + "^";
                        alert("load_Files" + load_Files);
                    } else
                    {
                        alert("Please select one checkbox to retransmission for loadtendering !!");
                    }
                }
            }
        }
        // alert("length-->"+po_Files);
        if ((load_Files != "") && (load_Files != null)) {
            var r = confirm("Please confirm retransmission of the selected Load Files!");
            if (r == true) {
                getLoadTenderCopy(load_Files, "POST", db);
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReTransmit");
        }
    }

    if ((btnValue.value == 'ReSubmit')) {
        var i = 0;
        var res;
        // alert("ReSubmit-->"+list);
        for (var j = 0; j < list; j++) {

            if (document.getElementById("check_List" + j) != null) {
                if (list == 1) {
                    res = document.getElementById("check_List" + j).checked;
                } else {
                    res = document.getElementById("check_List" + j).checked;
                }
                if (res == true) {
                    i = i + 1;
                    if (i == 1) {
                        load_Files = load_Files + document.getElementById("text" + j).value + "*";
                        load_Files = load_Files + document.getElementById("Instance" + j).value + "@";
                    } else
                    {
                        alert("Please select one checkbox to resubmit for loadtendering !!");
                    }
                }
            }
        }

        //alert("length-->"+po_Files);
        if ((load_Files != "") && (load_Files != null)) {
            var r = confirm("Please confirm resubmission of the selected Load Files!");
            if (r == true) {
                getLoadTenderCopy(load_Files, "PRE", db);
            } else {
                return false;
            }
        } else {
            alert("Please select checkbox(s) before ReSubmit");
        }
    }
}

function getLoadTenderCopy(Load_LIST, type, db) {
    //var req = new XMLHttpRequest();
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerString(req, populateLoadTenderCopy);
    var url = "../ajax/getLoadCopy.action?loadList=" + Load_LIST + "&type=" + type + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}
function populateLoadTenderCopy(responseText) {
    alert(responseText);
    document.getElementById("logisticsForm").submit();
}
/*
 *Get Payment Details
 */
function getPaymentDetails(fileId) {
    var db = document.forms["paymentForm"]["database"].value;
    $(function () {
        $('#detail_box').show();
        return false;
    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populatePaymentDetails);

    var url = "../ajax/getPaymentDetails.action?fileId=" + fileId + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populatePaymentDetails(responseXML)
{
    // var routings = detail.getElementsByTagName("ROUTINGS")[0].childNodes[0].nodeValue;  
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var fileid = detail.getElementsByTagName("FILE_ID")[0].childNodes[0].nodeValue;
        var docType = detail.getElementsByTagName("FILE_TYPE")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var TRAN_NUMBER = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var CHEQUE_NUMBER = detail.getElementsByTagName("Check_Number")[0].childNodes[0].nodeValue;
//        var PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
//        var CO_NUMBER = detail.getElementsByTagName("CO_NUMBER")[0].childNodes[0].nodeValue;
        var SEC_KEY_VAL = detail.getElementsByTagName("SEC_KEY_VAL")[0].childNodes[0].nodeValue;
        var INVOICE_NUMBER = detail.getElementsByTagName("INVOICE_NUMBER")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;
        // Sap Detals

        var SAP_DETAILS = detail.getElementsByTagName("SAP_DETAILS")[0].childNodes[0].nodeValue;
        var SAP_USER;
        var IDOC_NUMBER;
        var PO_NUMBER;
        var PO_DATE;
        var IDOC_STATUS_CODE;
        var IDOC_STATUS_DESCRIPTION;
        if (SAP_DETAILS != 'NO') {

            SAP_USER = detail.getElementsByTagName("SAP_USER")[0].childNodes[0].nodeValue;
            IDOC_NUMBER = detail.getElementsByTagName("IDOC_NUMBER")[0].childNodes[0].nodeValue;
            PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
            PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_CODE = detail.getElementsByTagName("IDOC_STATUS_CODE")[0].childNodes[0].nodeValue;
            IDOC_STATUS_DESCRIPTION = detail.getElementsByTagName("IDOC_STATUS_DESCRIPTION")[0].childNodes[0].nodeValue;

        }


        document.getElementById('instanceid').value = fileid;
        //document.getElementById('shipment').value=PRI_KEY_VAL;
        if (SEC_KEY_VAL != "NO") {
            document.getElementById('po').value = SEC_KEY_VAL;
        } else
        {
            document.getElementById('po').value = "--";
        }

        if (INVOICE_NUMBER != "NO") {
            document.getElementById('invoice').value = INVOICE_NUMBER;
        } else
        {
            document.getElementById('invoice').value = "--";
        }

        if (CHEQUE_NUMBER != "NO") {
            document.getElementById('Check_Number').value = CHEQUE_NUMBER;
        } else {
            document.getElementById('Check_Number').value = "--";
        }
        document.getElementById('documenttype').value = docType;
        document.getElementById('transactiontype').value = TRAN_NUMBER;
        document.getElementById('senderid').value = SENDER_ID;
        document.getElementById('sendername').value = SENDER_NAME;
        document.getElementById('receiverid').value = RECEIVER_ID;
        document.getElementById('receivername').value = RECEIVER_NAME;
        document.getElementById('isa').value = ISA_NUMBER;
        document.getElementById('gs').value = GS_CONTROL_NUMBER;
        document.getElementById('st').value = ST_CONTROL_NUMBER;
        document.getElementById('isadate').value = ISA_DATE;
        document.getElementById('isatime').value = ISA_TIME;
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('togglestatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('togglestatus').value = STATUS;
        } else {
            document.getElementById('togglestatus').value = STATUS;
        }
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('pretranfilepath').innerHTML = "--";
        } else {
            document.getElementById('pretranfilepath').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('posttranfilepath').innerHTML = "--";

        } else {
            document.getElementById('posttranfilepath').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ACKFILEID == "No File") {
            document.getElementById('ackfileid').innerHTML = "--";

        } else {
            document.getElementById('ackfileid').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('errormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('errormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
//        if (SAP_DETAILS != 'NO') {
//            document.getElementById('SAP_USER').value = SAP_USER;
//            document.getElementById('IDOC_NUMBER').value = IDOC_NUMBER;
//            document.getElementById('PO_NUMBER').value = PO_NUMBER;
//            document.getElementById('PO_DATE').value = PO_DATE;
//            document.getElementById('IDOC_STATUS_CODE').value = IDOC_STATUS_CODE;
//            document.getElementById('IDOC_STATUS_DESCRIPTION').value = IDOC_STATUS_DESCRIPTION;
//        }
//        else
//        {
//            document.getElementById('SAP_USER').value = '--';
//            document.getElementById('IDOC_NUMBER').value = '--';
//            document.getElementById('PO_NUMBER').value = '--';
//            document.getElementById('PO_DATE').value = '--';
//            document.getElementById('IDOC_STATUS_CODE').value = '--';
//            document.getElementById('IDOC_STATUS_DESCRIPTION').value = '--';
//        }

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}


/** 
 * New 
 * To add and update TP
 */
//for editing TP Details

function doEdit(id) {
    var id = id;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText1(req, populateTpDetails);

    var url = "../ajax/getTpDetails.action?tpId=" + id;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateTpDetails(responseXML)
{

    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];

        var ID = detail.getElementsByTagName("ID")[0].childNodes[0].nodeValue;
        // var Check_Number = detail.getElementsByTagName("Check_Number")[0].childNodes[0].nodeValue; 
        var NAME = detail.getElementsByTagName("NAME")[0].childNodes[0].nodeValue;
        var CONTACT_INFO = detail.getElementsByTagName("CONTACT_INFO")[0].childNodes[0].nodeValue;

        var VENDOR_NUMBER = detail.getElementsByTagName("VENDOR_NUMBER")[0].childNodes[0].nodeValue;

        var DEPARTMENTS = detail.getElementsByTagName("DEPARTMENTS")[0].childNodes[0].nodeValue;
        var EDI_COMM_ID = detail.getElementsByTagName("EDI_COMM_ID")[0].childNodes[0].nodeValue;
        var QUALIFIER = detail.getElementsByTagName("QUALIFIER")[0].childNodes[0].nodeValue;

        document.getElementById('id').value = ID;
        document.getElementById('id').disabled = true;
        document.getElementById('name').value = NAME;
        document.getElementById('contact').value = CONTACT_INFO;
        document.getElementById('phno').value = VENDOR_NUMBER;
        document.getElementById('dept').value = DEPARTMENTS;
        document.getElementById('commid').value = EDI_COMM_ID;
        document.getElementById('qualifier').value = QUALIFIER;
        document.getElementById("add").style.display = 'none';
        document.getElementById("update").style.display = 'table-row';

    }
}
function doTpUpdate()
{
    //  alert("do up");
    var id = document.getElementById("id").value;
    var name = document.getElementById("name").value;
    if (((id != null) && (id != "")) && (name != null) && (name != ""))
    {
        //  alert("in do Update");

        var id = document.getElementById('id').value;
        document.getElementById('id').disabled = false;
        var name = document.getElementById('name').value;
        var contact = document.getElementById('contact').value;
        var phno = document.getElementById('phno').value;
        var dept = document.getElementById('dept').value;
        var commid = document.getElementById('commid').value;
        var qualifier = document.getElementById('qualifier').value;

        var req = getXMLHttpRequest();
        req.onreadystatechange = readyStateHandlerString(req, showResultForUpdate);

        var url = "../ajax/updateTpDetails.action?tpId=" + id + "&name=" + name + "&contact=" + contact + "&phno=" + phno + "&dept=" + dept + "&commid=" + commid + "&qualifier=" + qualifier;
        req.open("GET", url, "true");
        req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        req.send(null);

    } else
    {
        alert("Please enter atleast Id and Name!!!");
    }
}

function showResultForUpdate(result)
{
    alert(result);
    document.getElementById('id').value = "";
    document.getElementById('id').value = "";
    document.getElementById('name').value = "";
    document.getElementById('contact').value = "";
    document.getElementById('phno').value = "";
    document.getElementById('dept').value = "";
    document.getElementById('commid').value = "";
    document.getElementById('qualifier').value = "";
    document.getElementById("add").style.display = 'table-row';
    document.getElementById("update").style.display = 'none';

}



function getTpDetailInformation(tpId)
{
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var tpname = name;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateTpDetailInformation);

    var url = "../ajax/getTpDetailInformation.action?tpId=" + tpId;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);

}

function populateTpDetailInformation(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var id = detail.getElementsByTagName("ID")[0].childNodes[0].nodeValue;
        var city = detail.getElementsByTagName("CITY")[0].childNodes[0].nodeValue;
        var zip = detail.getElementsByTagName("ZIP")[0].childNodes[0].nodeValue;

        //added 
        var vendor = detail.getElementsByTagName("VENDOR")[0].childNodes[0].nodeValue;

        //var vendor = detail.getElementByTagName("VENDOR")[0].childNodes[0].nodeValue;
        var department = detail.getElementsByTagName("DEPARTMENT")[0].childNodes[0].nodeValue;

        var ship = detail.getElementsByTagName("SHIP")[0].childNodes[0].nodeValue;
        var payDuns = detail.getElementsByTagName("PAY_DUNS")[0].childNodes[0].nodeValue;
        var order = detail.getElementsByTagName("ORDER")[0].childNodes[0].nodeValue;
        var url = detail.getElementsByTagName("URL")[0].childNodes[0].nodeValue;
        //  var cert = detail.getElementsByTagName("CERT")[0].childNodes[0].nodeValue;

        //alert("ACKFILEID-->"+ACKFILEID);
        // var routings = detail.getElementsByTagName("ROUTINGS")[0].childNodes[0].nodeValue;  

        var details = "<table style='margin: 0 0 0 0;padding: 0px 0px;'> ";
        if (id != "NO") {
            details = details + "<tr><td class='ajaxTd'>ID&nbsp;# :</td><td class='ajaxTd'>" + id + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>ID&nbsp;# :</td><td class='ajaxTd'>--</td></tr>";
        }
        if (city != "NO") {
            details = details + "<tr><td class='ajaxTd'>City&nbsp; :</td><td class='ajaxTd'>" + city + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>City&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }
        if (zip != "NO") {
            details = details + "<tr><td class='ajaxTd'>Zip&nbsp; :</td><td class='ajaxTd'>" + zip + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Zip&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }


        if (vendor != "NO") {
            details = details + "<tr><td class='ajaxTd'>Vendor&nbsp;#&nbsp;:</td><td class='ajaxTd'>" + vendor + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Vendor&nbsp;Number :</td><td class='ajaxTd'>--</td></tr>";
        }

        if (department != "NO") {
            details = details + "<tr><td class='ajaxTd'>Department&nbsp; :</td><td class='ajaxTd'>" + department + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Department&nbsp;Number&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }




        if (ship != "NO") {
            details = details + "<tr><td class='ajaxTd'>Ship&nbsp;Duns&nbsp; :</td><td class='ajaxTd'>" + ship + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Ship&nbsp;Duns&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }
        if (payDuns != "NO") {
            details = details + "<tr><td class='ajaxTd'>Pay&nbsp;Duns&nbsp; :</td><td class='ajaxTd'>" + payDuns + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Billings&nbsp;Duns&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }

        if (order != "NO") {
            details = details + "<tr><td class='ajaxTd'>Order&nbsp;Duns&nbsp; :</td><td class='ajaxTd'>" + order + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>Order&nbsp;Duns :</td><td class='ajaxTd'>--</td></tr>";
        }

        if (url != "NO") {
            details = details + "<tr><td class='ajaxTd'>URL&nbsp; :</td><td class='ajaxTd'>" + url + "</td></tr>";
        } else
        {
            details = details + "<tr><td class='ajaxTd'>URL&nbsp; :</td><td class='ajaxTd'>--</td></tr>";
        }





        // "<h5 >PRE_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+PRE_TRANS_FILEPATH+"\">PRE_TRANS_FILE</a></h5>"+
        //  "<h5 >POST_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+POST_TRANS_FILEPATH+"\">POST_TRANS_FILE</a></h5>";

    } //if
    if (chk.childNodes[0].nodeValue == "false") {
        var details = " <h5 >Sorry ! No Results Found</h5>";

    }
    var detailsDIV = document.getElementById("detailInformation");
    detailsDIV.innerHTML = "";
    detailsDIV.innerHTML = details;
    /*  $(function() {
     
     $('#detail_box').show();
     return false;
     
     });*/

}

function getLogisticsDocDetails(val, id) {
    var db = document.forms["documentForm"]["database"].value;
    var num = val;
    var id = id;
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLogisticsDocDetails);
    var url = "../ajax/getLogisticsDocDetails.action?isaNumber=" + num + "&id=" + id + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateLogisticsDocDetails(responseXML)
{
    //   alert("responseXML--->"+responseXML.toString());
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('LogDocErrorMessage').innerHTML = "";
        var detail = details.childNodes[0];
        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var docType = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;

        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        //            alert(PRE_TRANS_FILEPATH);
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        //             alert(POST_TRANS_FILEPATH);
        var SENDER_ID = detail.getElementsByTagName("SENDERID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVERID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;

        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;

        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var TRAN_NUMBER = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var SEC_KEY_VAL = detail.getElementsByTagName("SEC_KEY_VAL")[0].childNodes[0].nodeValue;
        var PRI_KEY_TYPE = detail.getElementsByTagName("PRI_KEY_TYPE")[0].childNodes[0].nodeValue;
        var PRI_KEY_VAL = detail.getElementsByTagName("PRI_KEY_VAL")[0].childNodes[0].nodeValue;
        var ORG_FILEPATH = detail.getElementsByTagName("ORG_FILEPATH")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        //  var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        //  var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;

        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;

        var BOL_NUMBER = detail.getElementsByTagName("BOL_NUMBER")[0].childNodes[0].nodeValue;
        var CO_NUMBER = detail.getElementsByTagName("CO_NUMBER")[0].childNodes[0].nodeValue;
        var PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;

        document.getElementById('LogDocInstanceId').value = fileid;
        document.getElementById('LogDocShipment').value = PRI_KEY_VAL;
        document.getElementById('LogDocDocumentType').value = docType;
        document.getElementById('LogDocTransactionType').value = TRAN_NUMBER;
        document.getElementById('LogDocSenderid').value = SENDER_ID;
        document.getElementById('LogDocSenderName').value = SENDER_NAME;
        document.getElementById('LogDocReceiverId').value = RECEIVER_ID;
        document.getElementById('LogDocReceiverName').value = RECEIVER_NAME;
        document.getElementById('LogDocIsa').value = ISA_NUMBER;
        document.getElementById('LogDocGs').value = GS_CONTROL_NUMBER;
        document.getElementById('LogDocSt').value = ST_CONTROL_NUMBER;
        document.getElementById('LogDocIsADate').value = ISA_DATE;
        document.getElementById('LogDocIsATime').value = ISA_TIME;
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('LogDocDetailInfoStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('LogDocDetailInfoStatus').value = STATUS;
        } else {
            document.getElementById('LogDocDetailInfoStatus').value = STATUS;
        }


        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('LogDocPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('LogDocPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('LogDocPostTranslation').innerHTML = "--";

        } else {
            document.getElementById('LogDocPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('LogDocAckFileId').innerHTML = "--";

        } else {
            document.getElementById('LogDocAckFileId').innerHTML = "<a style=\"color: deepskyblue\" style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('LogDocErrorMessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('LogDocErrorMessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');
}

function getLoadTenderingDetails(number, ponum) {
    //alert("hai");
    //alert("hiii222222"+number+"ponum------>"+ponum);
    var db = document.forms["logisticsForm"]["database"].value;
    var num = number;
    var ponum = ponum;
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLoadTenderingDetails);
    var url = "../ajax/getLoadTenderingDetails.action?isaNumber=" + num + "&poNumber=" + ponum + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateLoadTenderingDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    // alert("details--->"+details);
    var detail = details.childNodes[0];
    // alert("responseXML--->"+detail.getElementsByTagName("SEC_KEY_VAL")[0].childNodes[0].nodeValue);
    var chk = detail.getElementsByTagName("VALID")[0];


    // alert(confirmMessage);


    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('errormessage').innerHTML = "";
        var detail = details.childNodes[0];

        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var docType = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDERID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVERID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;
        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        //var TRAN_NUMBER = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue; 
        //var SEC_KEY_VAL = detail.getElementsByTagName("SEC_KEY_VAL")[0].childNodes[0].nodeValue; 
        //var PRI_KEY_TYPE = detail.getElementsByTagName("PRI_KEY_TYPE")[0].childNodes[0].nodeValue; 
        var PRI_KEY_VAL = detail.getElementsByTagName("PRI_KEY_VAL")[0].childNodes[0].nodeValue;
        //var ORG_FILEPATH = detail.getElementsByTagName("ORG_FILEPATH")[0].childNodes[0].nodeValue; 
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        //var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;

        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var totVolume = detail.getElementsByTagName("TOTAL_VOLUME")[0].childNodes[0].nodeValue;
        var totWeight = detail.getElementsByTagName("TOTAL_WEIGHT")[0].childNodes[0].nodeValue;
        var totpieces = detail.getElementsByTagName("TOTAL_PIECES")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;


        document.getElementById('instanceid').value = fileid;
        document.getElementById('shipment').value = PRI_KEY_VAL;
        //        if(PO_NUMBER != "NO"){
        //            document.getElementById('po').value=PO_NUMBER;
        //        }
        //        else
        //        {
        //            document.getElementById('po').value="--";
        //        }
        //            
        //        if(CO_NUMBER != "NO"){
        //            document.getElementById('co').value=CO_NUMBER;
        //        }
        //        else
        //        {
        //            document.getElementById('co').value="--";
        //        }
        //document.getElementById('Volume').value = totVolume;
        document.getElementById('totWeight').value = totWeight;
        document.getElementById('pieces').value = totpieces;
        document.getElementById('documenttype').value = docType;
        document.getElementById('transactiontype').value = TRANSACTION_TYPE;
        document.getElementById('senderid').value = SENDER_ID;
        document.getElementById('sendername').value = SENDER_NAME;
        document.getElementById('receiverid').value = RECEIVER_ID;
        document.getElementById('receivername').value = RECEIVER_NAME;
        document.getElementById('isa').value = ISA_NUMBER;
        document.getElementById('gs').value = GS_CONTROL_NUMBER;
        document.getElementById('st').value = ST_CONTROL_NUMBER;
        document.getElementById('isadate').value = ISA_DATE;
        document.getElementById('isatime').value = ISA_TIME;

        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('togglestatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('togglestatus').value = STATUS;
        } else {
            document.getElementById('togglestatus').value = STATUS;
        }

        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('pretranslation').innerHTML = "--";
        } else {
            document.getElementById('pretranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('posttranslation').innerHTML = "--";

        } else {
            document.getElementById('posttranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('ackfileid').innerHTML = "--";

        } else {
            document.getElementById('ackfileid').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('errormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('errormessage').innerHTML = "--";
        }

        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}



function getLtResponseDetails(fileId, refId) {
    //  alert("hii");
    //var num=number;
    //alert("inv number-->"+num);
    //  var req = new XMLHttpRequest();

    var db = document.forms["ltResponseForm"]["database"].value;

    $(function () {
        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLtResponseDetails);

    var url = "../ajax/getLtResponseDetails.action?fileId=" + fileId + "&refId=" + refId + "&database=" + db;


    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateLtResponseDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('resErrormessage').innerHTML = "";
        var detail = details.childNodes[0];
        var FILE_ID = detail.getElementsByTagName("FILE_ID")[0].childNodes[0].nodeValue;
        var SHIPMENT_ID = detail.getElementsByTagName("SHIPMENT_ID")[0].childNodes[0].nodeValue;
        var FILE_TYPE = detail.getElementsByTagName("FILE_TYPE")[0].childNodes[0].nodeValue;
        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;
        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var REFERENCE = detail.getElementsByTagName("REFERENCE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        // var ORGFILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue;

        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        var ERR_MESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;



        // alert(deilvaryName+" "+poValue+ " "+ routings+ " "+invoice+" "+itemQty);


        document.getElementById('resInstanceid').value = FILE_ID;
        document.getElementById('resShipment').value = SHIPMENT_ID;
        document.getElementById('resFiletype').value = FILE_TYPE;
        document.getElementById('resTransactiontype').value = TRANSACTION_TYPE;
        document.getElementById('resSenderid').value = SENDER_ID;
        document.getElementById('resSendername').value = SENDER_NAME;
        document.getElementById('resReceiverid').value = RECEIVER_ID;
        document.getElementById('resReceivername').value = RECEIVER_NAME;
        document.getElementById('resIsa').value = ISA_NUMBER;
        document.getElementById('resGs').value = GS_CONTROL_NUMBER;
        document.getElementById('resSt').value = ST_CONTROL_NUMBER;
        document.getElementById('resIsadate').value = ISA_DATE;
        document.getElementById('resIsatime').value = ISA_TIME;
        document.getElementById('resReference').value = REFERENCE;
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('resStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('resStatus').value = STATUS;
        } else {
            document.getElementById('resStatus').value = STATUS;
        }
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('resPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('resPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('resPostTranslation').innerHTML = "--";

        } else {
            document.getElementById('resPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('resAckfileid').innerHTML = "--";

        } else {
            document.getElementById('resAckfileid').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERR_MESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('resErrormessage').innerHTML = ERR_MESSAGE;
        } else {
            document.getElementById('resErrormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}



function getLogisticsInvDetails(number, id) {
    var num = number;
    var id = id;
    var db = document.forms["logisticsForm"]["database"].value;
    //alert("inv number-->"+num);
    //  var req = new XMLHttpRequest();
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLogisticsInvDetails);

    var url = "../ajax/getLogisticsInvDetails.action?invNumber=" + num + "&id=" + id + "&database=" + db;

    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateLogisticsInvDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('InvErrormessage').value = "";
        var detail = details.childNodes[0];
        var fileID = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var invNum = detail.getElementsByTagName("INVNUMBER")[0].childNodes[0].nodeValue;
        var poNum = detail.getElementsByTagName("PONUMBER")[0].childNodes[0].nodeValue;
        var itemQty = detail.getElementsByTagName("ITEMQTY")[0].childNodes[0].nodeValue;
        var invAmt = detail.getElementsByTagName("INVAMT")[0].childNodes[0].nodeValue;
        var isaNum = detail.getElementsByTagName("ISANUM")[0].childNodes[0].nodeValue;
        var isaDate = detail.getElementsByTagName("ISADATE")[0].childNodes[0].nodeValue;
        var isaTime = detail.getElementsByTagName("ISATIME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var ORGFILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;
        var FILETYPE = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;

        // alert(deilvaryName+" "+poValue+ " "+ routings+ " "+invoice+" "+itemQty);

        document.getElementById('InvInstanceid').value = fileID;
        document.getElementById('InvPo').value = poNum;
        document.getElementById('InvTransactiontype').value = TRANSACTION_TYPE;
        document.getElementById('InvNum').value = invNum;
        document.getElementById('InvItemQty').value = itemQty;
        document.getElementById('InvAmt').value = invAmt;
        document.getElementById('InvDocpe').value = FILETYPE;
        /* if (ORGFILEPATH == "No File") {
         document.getElementById('InvORGFILEPATH').innerHTML = "--";
         } else {
         document.getElementById('InvORGFILEPATH').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + ORGFILEPATH + "\">Download</a></td></tr>";
         }*/
        document.getElementById('InvSenderid').value = SENDER_ID;
        document.getElementById('InvSendername').value = SENDER_NAME;
        document.getElementById('InvReceiverid').value = RECEIVER_ID;
        document.getElementById('InvReceivername').value = RECEIVER_NAME;
        document.getElementById('InvIsa').value = isaNum;
        document.getElementById('InvGs').value = GS_CONTROL_NUMBER;
        document.getElementById('InvSt').value = ST_CONTROL_NUMBER;
        document.getElementById('InvIsadate').value = isaDate;
        document.getElementById('InvIsatime').value = isaTime;
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('InvStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('InvStatus').value = STATUS;
        } else {
            document.getElementById('InvStatus').value = STATUS;
        }
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('InvPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('InvPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('InvPostTranslation').innerHTML = "--";

        } else {
            document.getElementById('InvPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('InvAckfileid').innerHTML = "--";

        } else {
            document.getElementById('InvAckfileid').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

//    if (ERRMESSAGE != "NO MSG") {
//        document.getElementById('InvErrormessage').value = ERRMESSAGE;
//
//    }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('InvErrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('InvErrormessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (chk.childNodes[0].nodeValue == "false") {
            document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


        }
        $('#hide-menu1').addClass('show-menu');

    }
}


/* Method for displaying Details Information of LogisticsShipment
 * Date : 06/27/2013
 * Author : santosh kola
 * 
 */
function getLogisticsShipmentDetails(number, ponum, id) {
    var db = document.forms["ltshipmentForm"]["database"].value;
    var num = number;
    var ponumber = ponum;
    var id = id;
    //alert("this general ajax num-->"+num);
    //alert("this general ajax ponumber-->"+ponumber);
    //alert("this general ajax id-->"+id);
    //  var req = new XMLHttpRequest(); &id="+id
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLogisticsShipmentDetails);

    var url = "../ajax/getLogisticsShipmentDetails.action?asnNumber=" + num + "&poNumber=" + ponum + "&id=" + id + "&database=" + db;

    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}



function populateLogisticsShipmentDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    //    alert(chk.childNodes[0].nodeValue );

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('LSErrorMessage').innerHTML = "";
        var detail = details.childNodes[0];
        var fileID = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var asnNum = detail.getElementsByTagName("ASNNUMBER")[0].childNodes[0].nodeValue;
        var poNum = detail.getElementsByTagName("PONUMBER")[0].childNodes[0].nodeValue;
        var itemQty = detail.getElementsByTagName("ITEMQTY")[0].childNodes[0].nodeValue;
        var asnVolume = detail.getElementsByTagName("ASNVOLUME")[0].childNodes[0].nodeValue;
        var isaNum = detail.getElementsByTagName("ISANUM")[0].childNodes[0].nodeValue;
        var isaDate = detail.getElementsByTagName("ISADATE")[0].childNodes[0].nodeValue;
        var isaTime = detail.getElementsByTagName("ISATIME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var ORGFILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ERROR_REPORT_FILEPATH = detail.getElementsByTagName("ERROR_REPORT_FILEPATH")[0].childNodes[0].nodeValue;
        var FILETYPE = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;

        // alert(deilvaryName+" "+poValue+ " "+ routings+ " "+invoice+" "+itemQty);
        document.getElementById('LSInstanceId').value = fileID;
        document.getElementById('LSAsnnumber').value = asnNum;
        document.getElementById('LSPo').value = poNum;
        //document.getElementById('LSItemqty').value = itemQty;
        //document.getElementById('LSAsnvolume').value = asnVolume;
        document.getElementById('LSIsANumber').value = isaNum;
        document.getElementById('LSIsADate').value = isaDate;
        document.getElementById('LSIsATime').value = isaTime;
        document.getElementById('LSSenderId').value = SENDER_ID;
        document.getElementById('LSSenderName').value = SENDER_NAME;
        document.getElementById('LSReceiverId').value = RECEIVER_ID;
        document.getElementById('LSReceiverName').value = RECEIVER_NAME;
        document.getElementById('LSGs').value = GS_CONTROL_NUMBER;
        document.getElementById('LSSt').value = ST_CONTROL_NUMBER;
        document.getElementById('LSDocType').value = FILETYPE;

        document.getElementById('LSTransactionType').value = TRANSACTION_TYPE;

        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('LSDetailInfoStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('LSDetailInfoStatus').value = STATUS;
        } else {
            document.getElementById('LSDetailInfoStatus').value = STATUS;
        }


        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('LSPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('LSPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('LSPostTranslation').innerHTML = "--";
        } else {
            document.getElementById('LSPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        /* if (ORGFILEPATH == "No File") {
         document.getElementById('LSOrgFilePath').innerHTML = "--";
         
         } else {
         document.getElementById('LSOrgFilePath').innerHTML = "<a href=\"../download/getAttachment.action?locationAvailable=" + ORGFILEPATH + "\">Download</a>";
         }*/
        if (ACKFILEID == "No File") {
            document.getElementById('LSAckFileid').innerHTML = "--";

        } else {
            document.getElementById('LSAckFileid').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

//        if (ERRMESSAGE != "NO MSG") {
//            
//            document.getElementById('LSErrorMessage').innerHTML = ERRMESSAGE;
//
//        }
        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('LSErrorMessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('LSErrorMessage').innerHTML = "--";
        }
        if (ERROR_REPORT_FILEPATH == "No File") {
            document.getElementById('ErrReport').innerHTML = "--";
        } else {
            document.getElementById('ErrReport').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ERROR_REPORT_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }
    $('#hide-menu1').addClass('show-menu');
}

function getDocVisibilityDetails(id) {
    //alert("Id -->"+id);
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateDocVisibilityDetails);

    var url = "../ajax/getDocVisibilityDetails.action?docId=" + id;

    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateDocVisibilityDetails(responseXML) {
    //    alert(responseXML.toString());
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('errormessage').innerHTML = "";
        var detail = details.childNodes[0];
        var FILE_ID = detail.getElementsByTagName("FILE_ID")[0].childNodes[0].nodeValue;
        var PARENT_FILE_ID = detail.getElementsByTagName("PARENT_FILE_ID")[0].childNodes[0].nodeValue;
        var FILE_TYPE = detail.getElementsByTagName("FILE_TYPE")[0].childNodes[0].nodeValue;
        var FILE_ORIGIN = detail.getElementsByTagName("FILE_ORIGIN")[0].childNodes[0].nodeValue;
        var TRAN_MESS_TYPE = detail.getElementsByTagName("TRAN_MESS_TYPE")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;
        var INTERCHANGE_CONTROLNO = detail.getElementsByTagName("INTERCHANGE_CONTROLNO")[0].childNodes[0].nodeValue;
        var FUNCTIONAL_CONTROLNO = detail.getElementsByTagName("FUNCTIONAL_CONTROLNO")[0].childNodes[0].nodeValue;
        var MESSAGE_CONTROLNO = detail.getElementsByTagName("MESSAGE_CONTROLNO")[0].childNodes[0].nodeValue;
        var DATE_TIME_RECEIVED = detail.getElementsByTagName("DATE_TIME_RECEIVED")[0].childNodes[0].nodeValue;
        var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var ERR_MESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;

        var ACK_STATUS = detail.getElementsByTagName("ACK_STATUS")[0].childNodes[0].nodeValue;
        var ID = detail.getElementsByTagName("ID")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var appfieldsList = detail.getElementsByTagName("APPFIELDS")[0];
        var appFiled = appfieldsList.getElementsByTagName("APPFIELD");

        for (var i = 0; i < appFiled.length; i++) {
            //alert("123");
            var appFiledInfo = appFiled[i];
            var att = appFiledInfo.getAttribute("label");
            var name = appFiledInfo.firstChild.nodeValue;
            document.getElementById('DocAppField' + i).value = name;
        }


        document.getElementById('DocInstanceid').value = FILE_ID;
        document.getElementById('DocParentFileId').value = PARENT_FILE_ID;

        document.getElementById('DocFileType').value = FILE_TYPE;


        document.getElementById('DocFileOrigin').value = FILE_ORIGIN;
        document.getElementById('DocFunctionalControlNo').value = FUNCTIONAL_CONTROLNO;
        document.getElementById('DocTranMessType').value = TRAN_MESS_TYPE;
        document.getElementById('DocSenderid').value = SENDER_ID;
        document.getElementById('DocInterchangeControlNo').value = INTERCHANGE_CONTROLNO;
        document.getElementById('DocReceiverid').value = RECEIVER_ID;
        document.getElementById('DocMessageControlNo').value = MESSAGE_CONTROLNO;
        document.getElementById('DocDateTimeReceived').value = DATE_TIME_RECEIVED;
        document.getElementById('DocDirection').value = DIRECTION;
        document.getElementById('DocAckStatus').value = ACK_STATUS;
        document.getElementById('DocId').value = ID;
        document.getElementById('DocIsaTime').value = ISA_TIME;
        document.getElementById('DocIsaDate').value = ISA_DATE;
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('DocDetailInfoStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('DocDetailInfoStatus').value = STATUS;
        } else {
            document.getElementById('DocDetailInfoStatus').value = STATUS;
        }

        if (ERR_MESSAGE != "NO MSG") {
            document.getElementById('errormessage').innerHTML = ERR_MESSAGE;

        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').innerHTML = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');
}

/*Method for Routing list
 * Date : 02/03/2015
 * Author : Santosh Kola
 */

function changeDestLabel(element) {
    // alert(element.value);
    if (element.value == 'INBOUND') {
        document.getElementById("directionLabel").innerHTML = 'INBOUND';
    } else {
        document.getElementById("directionLabel").innerHTML = 'OUTBOUND';
    }

}

function loadDestLabel() {
    // alert(element.value);
    var direction = document.getElementById("direction").value;
    if (direction == 'INBOUND') {
        document.getElementById("directionLabel").innerHTML = 'INBOUND';
    } else if (direction == 'OUTBOUND') {
        document.getElementById("directionLabel").innerHTML = 'OUTBOUND';
    }

}

/*Displaying Partner details
 * Author : Santosh Kola
 * Date : 02/03/2015
 */

function getPartnerDetails(partnerId) {
    // alert("hii");

    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populatePartnerDetails);
    var url = "../ajax/getPartnerDetails.action?partnerId=" + partnerId;
    req.open("POST", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function populatePartnerDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var TP_ID = detail.getElementsByTagName("TP_ID")[0].childNodes[0].nodeValue;
        var TP_NAME = detail.getElementsByTagName("TP_NAME")[0].childNodes[0].nodeValue;
        var INTERNALIDENTIFIER = detail.getElementsByTagName("INTERNALIDENTIFIER")[0].childNodes[0].nodeValue;
        var APPLICATIONID = detail.getElementsByTagName("APPLICATIONID")[0].childNodes[0].nodeValue;
        var STATE = detail.getElementsByTagName("STATE")[0].childNodes[0].nodeValue;
        var MODIFIED_TS = detail.getElementsByTagName("MODIFIED_TS")[0].childNodes[0].nodeValue;
        var MODIFIED_BY = detail.getElementsByTagName("MODIFIED_BY")[0].childNodes[0].nodeValue;
        var CREATED_TS = detail.getElementsByTagName("CREATED_TS")[0].childNodes[0].nodeValue;

        document.getElementById('dpartnerId').value = TP_ID;
        document.getElementById('dpartnerName').value = TP_NAME;
        document.getElementById('dinternalIdentifier').value = INTERNALIDENTIFIER;
        document.getElementById('dapplicationId').value = APPLICATIONID;
        document.getElementById('dcountryCode').value = STATE;
        document.getElementById('dcreatedDate').value = CREATED_TS;
        document.getElementById('dchangedDate').value = MODIFIED_TS;
        document.getElementById('dchangedBy').value = MODIFIED_BY;
        //alert("ACKFILEID-->"+ACKFILEID);
        // var routings = detail.getElementsByTagName("ROUTINGS")[0].childNodes[0].nodeValue;  
        $('#hide-menu1').addClass('show-menu');

        // "<h5 >PRE_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+PRE_TRANS_FILEPATH+"\">PRE_TRANS_FILE</a></h5>"+
        //  "<h5 >POST_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+POST_TRANS_FILEPATH+"\">POST_TRANS_FILE</a></h5>";

    } //if
    if (chk.childNodes[0].nodeValue == "false") {
        var details = " <h5 >Sorry ! No Results Found</h5>";

    }
    var detailsDIV = document.getElementById("detailInformation");
    detailsDIV.innerHTML = "";
    detailsDIV.innerHTML = details;
    /*  $(function() {
     
     $('#detail_box').show();
     return false;
     
     });*/

}

function getRoutingDetails(routingId) {
    // alert("hii");
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateRoutingDetails);
    var url = "../ajax/getRoutingDetails.action?routingId=" + routingId;
    req.open("POST", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function populateRoutingDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var ROUTER_NAME = detail.getElementsByTagName("ROUTER_NAME")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var ACCEPTORLOOKUPALIAS = detail.getElementsByTagName("ACCEPTORLOOKUPALIAS")[0].childNodes[0].nodeValue;
        var INTERNALROUTEREMAIL = detail.getElementsByTagName("INTERNALROUTEREMAIL")[0].childNodes[0].nodeValue;
        var DESTMAILBOX = detail.getElementsByTagName("DESTMAILBOX")[0].childNodes[0].nodeValue;
        var SYSTEMTYPE = detail.getElementsByTagName("SYSTEMTYPE")[0].childNodes[0].nodeValue;
        var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;
        var ENVELOPE = detail.getElementsByTagName("ENVELOPE")[0].childNodes[0].nodeValue;
        var CREATEDDATE = detail.getElementsByTagName("CREATEDDATE")[0].childNodes[0].nodeValue;
        var MODIFIEDDATE = detail.getElementsByTagName("MODIFIEDDATE")[0].childNodes[0].nodeValue;

        document.getElementById('rrouterName').value = ROUTER_NAME;
        document.getElementById('rrouterStatus').value = STATUS;

        document.getElementById('racceptorLookUpAlias').value = ACCEPTORLOOKUPALIAS;
        document.getElementById('rinternalRouteEmail').value = INTERNALROUTEREMAIL;
        document.getElementById('rdestMailBox').value = DESTMAILBOX;
        document.getElementById('rsystemType').value = SYSTEMTYPE;
        document.getElementById('rdirection').value = DIRECTION;
        document.getElementById('renvelope').value = ENVELOPE;
        document.getElementById('rcreatedDate').value = CREATEDDATE;
        document.getElementById('rchangedDate').value = MODIFIEDDATE;

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}




function getB2bDetailInformation(b2bChannelId) {
    // alert("hii");

    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateB2bChannelDetails);
    var url = "../ajax/getB2bChannelDetails.action?b2bChannelId=" + b2bChannelId;
    req.open("POST", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function populateB2bChannelDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var TP_ID = detail.getElementsByTagName("TP_ID")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;
        var PROTOCOL = detail.getElementsByTagName("PROTOCOL")[0].childNodes[0].nodeValue;
        var HOST = detail.getElementsByTagName("HOST")[0].childNodes[0].nodeValue;
        var USERNAME = detail.getElementsByTagName("USERNAME")[0].childNodes[0].nodeValue;
        var PRODUCERMAILBOX = detail.getElementsByTagName("PRODUCERMAILBOX")[0].childNodes[0].nodeValue;
        var CONSUMERMAILBOX = detail.getElementsByTagName("CONSUMERMAILBOX")[0].childNodes[0].nodeValue;
        var POOLINGCODE = detail.getElementsByTagName("POOLINGCODE")[0].childNodes[0].nodeValue;
        var APPID = detail.getElementsByTagName("APPID")[0].childNodes[0].nodeValue;
        var SENDERID = detail.getElementsByTagName("SENDERID")[0].childNodes[0].nodeValue;
        var RECEIVERID = detail.getElementsByTagName("RECEIVERID")[0].childNodes[0].nodeValue;
        // var APPID = detail.getElementsByTagName("APPID")[0].childNodes[0].nodeValue;


        document.getElementById('b2bpartnerName').value = TP_ID;
        document.getElementById('b2bstatus').value = STATUS;
        document.getElementById('b2bdirection').value = DIRECTION;
        document.getElementById('b2bprotocal').value = PROTOCOL;
        document.getElementById('b2bhost').value = HOST;
        document.getElementById('b2busername').value = USERNAME;
        document.getElementById('b2bproducermailbox').value = PRODUCERMAILBOX;
        document.getElementById('b2bconsumermailbox').value = CONSUMERMAILBOX;
        document.getElementById('b2bpoolingcode').value = POOLINGCODE;
        document.getElementById('b2bappid').value = APPID;
        document.getElementById('b2bsenderid').value = SENDERID;
        document.getElementById('b2breceiverid').value = RECEIVERID;
        //alert("ACKFILEID-->"+ACKFILEID);
        // var routings = detail.getElementsByTagName("ROUTINGS")[0].childNodes[0].nodeValue;  

        // "<h5 >PRE_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+PRE_TRANS_FILEPATH+"\">PRE_TRANS_FILE</a></h5>"+
        //  "<h5 >POST_TRANS_FILE :&nbsp; &nbsp; <a href=\"../download/getAttachment.action?locationAvailable="+POST_TRANS_FILEPATH+"\">POST_TRANS_FILE</a></h5>";
        $('#hide-menu1').addClass('show-menu');
    } //if
    if (chk.childNodes[0].nodeValue == "false") {
        var details = " <h5 >Sorry ! No Results Found</h5>";

    }
    var detailsDIV = document.getElementById("detailInformation");
    detailsDIV.innerHTML = "";
    detailsDIV.innerHTML = details;
    /*  $(function() {
     
     $('#detail_box').show();
     return false;
     
     });*/

}

/*Displaying DetailInfo 
 * Author : Santosh Kola
 * Date : 02/09/2015
 */


function getDeliveryChannelDetails(deliveryChannelId) {
    // alert("hii");
    $(function () {

        $('#detail_box').show();
        return false;

    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateDeliveryChannelDetails);
    var url = "../ajax/getDeliveryChannelDetails.action?deliveryChannelId=" + deliveryChannelId;
    req.open("POST", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}


function populateDeliveryChannelDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];

    // alert("datails--->"+details);
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var PARTNER_ID = detail.getElementsByTagName("PARTNER_ID")[0].childNodes[0].nodeValue;
        var PartnerName = detail.getElementsByTagName("PartnerName")[0].childNodes[0].nodeValue;
        var ROUTER_NAME = detail.getElementsByTagName("ROUTER_NAME")[0].childNodes[0].nodeValue;
        var bpName = detail.getElementsByTagName("bpName")[0].childNodes[0].nodeValue;
        var transName = detail.getElementsByTagName("transName")[0].childNodes[0].nodeValue;
        var demName = detail.getElementsByTagName("demName")[0].childNodes[0].nodeValue;
        var pmbName = detail.getElementsByTagName("pmbName")[0].childNodes[0].nodeValue;
        var encodingName = detail.getElementsByTagName("encodingName")[0].childNodes[0].nodeValue;
        var SEQUENCE = detail.getElementsByTagName("SEQUENCE")[0].childNodes[0].nodeValue;
        var ARCHIVEFLAG = detail.getElementsByTagName("ARCHIVEFLAG")[0].childNodes[0].nodeValue;
        var ARCHIVEDIRCTORY = detail.getElementsByTagName("ARCHIVEDIRCTORY")[0].childNodes[0].nodeValue;
        var OUTPUTFILENAME = detail.getElementsByTagName("OUTPUTFILENAME")[0].childNodes[0].nodeValue;
        var OUTPUTFORMAT = detail.getElementsByTagName("OUTPUTFORMAT")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;

        document.getElementById('partnerName').value = PartnerName;
        document.getElementById('routingName').value = ROUTER_NAME;

        document.getElementById('bpName').value = bpName;
        document.getElementById('translationMapName').value = transName;
        document.getElementById('documentExtraxctMapName').value = demName;
        document.getElementById('producerMailBox').value = pmbName;
        document.getElementById('encoding').value = encodingName;
        document.getElementById('sequence').value = SEQUENCE;
        document.getElementById('archiveFlag').value = ARCHIVEFLAG;
        document.getElementById('archiveDirectory').value = ARCHIVEDIRCTORY;
        document.getElementById('outputFileName').value = OUTPUTFILENAME;
        document.getElementById('outputFormat').value = OUTPUTFORMAT;
        document.getElementById('deliveryStatus').value = STATUS;

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');

}



/*Method for displaying Dashboard details
* Author : Santosh kola
* Date : 02/19/2015
*/

function getDashboardDeatls(flag) {
// alert("hii"+flag);
document.getElementById("tblCharts").style.display = 'none';
var startDate = document.getElementById("docdatepickerfrom").value;
// alert('startDate'+startDate);
var endDate = document.getElementById("docdatepicker").value;
// alert('endDate'+endDate);
// var docSenderId = document.getElementById("docSenderId").value;
// var direction = document.getElementById("direction").value;
var docType = document.getElementById("docType").value;

// var ackStatus = document.getElementById("ackStatus").value;
var status = document.getElementById("status").value;
var partnerId = document.getElementById("partnerMapId").value;
document.getElementById("gridDiv").style.display = 'block';

var req = getXMLHttpRequest();
req.onreadystatechange = readyStateHandlerLoadText2(req, populateDashboardDetails);
// var url="../ajax/getDashboardDetails.action?startDate="+startDate+"&endDate="+endDate+"&docType="+docType+"&ackStatus="+ackStatus+"&status="+status+"&partnerId="+partnerId+"&direction="+direction;
var url = "../ajax/getDashboardDetails.action?flag=" + flag + "&startDate=" + startDate + "&endDate=" + endDate + "&docType=" + docType + "&status=" + status + "&partnerId=" + partnerId;
//alert("url : "+url);
req.open("GET", url, "true");
req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
req.send(null);


}

function populateDashboardDetails(resText) {
//alert("resText "+resText);
var response = resText.split("*");
//alert("response[0] "+response[0]);
//alert("response[1] "+response[1]);
// var response = "6104917000CH|2^6183932991|3^925485US00|207^KNIG|58^LANDOLAKESDEMO|4^MSSDEMO|4^MSSDEMOSN|9^TPDEMORX|9^UNILEVER_E2U|58^*6104917000CH|4^6183932991|4^925485US00|0^KNIG|31^LANDOLAKESDEMO|6^MSSDEMO|6^MSSDEMOSN|1^TPDEMORX|1^UNILEVER_E2U|31^".split("*");
document.getElementById("inboundTrans").value = response[0];
document.getElementById("outboundTrans").value = response[1];
document.getElementById("tblCharts").style.display = 'block';
// google.setOnLoadCallback(drawInboundChart);
// google.setOnLoadCallback(drawOutboundChart);

drawInboundChart();
drawOutboundChart();

//document.getElementById("tblCharts").style.display='block';
// google.setOnLoadCallback(drawInboundChart);
//google.setOnLoadCallback(drawOutboundChart);
//alert(resText);
}

function drawInboundChart() {

//alert(response[0]);
//alert(response[1]);


var element = document.getElementById("inboundTrans").value;

// alert(element);
var inboudResponse = element.split("@");


var arraydata = [['PartnerName', 'INBOUND', {
role: 'annotation'
}]];
/* var data = google.visualization.arrayToDataTable([
['Task', 'Hours per Day'],
['Work', 11],
['Eat', 2],
['Commute', 2],
['Watch TV', 2],
['Sleep', 7]
]);
*/

for (var i = 0; i < inboudResponse.length - 1; i++) {
var res = inboudResponse[i].split("$");
var dArray = [res[0], parseInt(res[1]), ''];
arraydata.push(dArray);
}

var data = google.visualization.arrayToDataTable(arraydata);

var options = {
title: 'Partner Inbound Transactions'

};

var chart = new google.visualization.PieChart(document.getElementById('inboundPiechart'));
//var chart1 = new google.visualization.ColumnChart(document.getElementById('inboundPiechart'));
//chart.draw(data, options);
chart.draw(data, options);
// chart1.draw(data, options);
}

//----------------------
function drawOutboundChart() {
var element = document.getElementById("outboundTrans").value;
// alert(element);

var outboudResponse = element.split("@");

var arraydata = [['PartnerName', 'OUTBOUND']];
/* var data = google.visualization.arrayToDataTable([
['Task', 'Hours per Day'],
['Work', 11],
['Eat', 2],
['Commute', 2],
['Watch TV', 2],
['Sleep', 7]
]);
*/

for (var i = 0; i < outboudResponse.length - 1; i++) {
var res = outboudResponse[i].split("$");
var dArray = [res[0], parseInt(res[1])];
arraydata.push(dArray);
}

var data = google.visualization.arrayToDataTable(arraydata);

var options = {
title: 'Partner Outbound Transactions'
};

var chart = new google.visualization.PieChart(document.getElementById('outboundPiechart'));
//var chart1 = new google.visualization.ColumnChart(document.getElementById('outboundPiechart'));
chart.draw(data, options);
//chart1.draw(data, options);
}

//  new classe for schdular tasks
function getDeleteReport(id) {
    //document.getElementById("load").style.display = 'block';
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText(req, populateReportDeleteDetails);


    var url = "../ajax/getReportDeleteDetails.action?&id=" + id;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}


function populateReportDeleteDetails(responsetext)
{
    //alert(responsetext);
    document.getElementById("reportsattachForm").submit();

}


function BDMOverlay(id) {
    var overlay = document.getElementById('overlay');
    var specialBox1 = document.getElementById('specialBox');

    document.getElementById("headerLabel").style.color = "white";
    document.getElementById("headerLabel").innerHTML = "Download Report";



    overlay.style.opacity = -10;
    if (overlay.style.display == "block") {
        overlay.style.display = "none";
        specialBox1.style.display = "none";
    } else {
        overlay.style.display = "block";
        getOverlay(id);
        specialBox1.style.display = "block";
    }



}

function getOverlay(id) {
    var startDate = document.getElementById("schStartdate").value;
    //document.getElementById("load").style.display='block';
    var req = getXMLHttpRequest();
    document.getElementById("scheduleid").value = id;
    req.onreadystatechange = readyStateHandlerString(req, populateOverlayDetails);


    var url = "../ajax/getReportOverlayDetails.action?&id=" + id + "&startDate=" + startDate;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateOverlayDetails(responseText)
{
    //alert("test");
    //alert("response data--->"+responseText);

    if (responseText != "Nodata") {
        document.getElementById("downloadLink").style.display = 'block';
        document.getElementById("downloadMessage").style.display = 'none';
    } else {
        document.getElementById("downloadLink").style.display = 'none';
        document.getElementById("downloadMessage").style.display = 'block';
        document.getElementById("downloadMessage").innerHTML = "<font color='red'>No reports to Download</font>"
    }



}

function DownloadSchedulerReport() {
    //alert("DownloadSchedulerReport");
    var startDate = document.getElementById("schStartdate").value;
    var scheduleId = document.getElementById("scheduleid").value;
    window.location = "../download/reportDownloads.action?scheduleId=" + scheduleId + "&startDate=" + startDate;
}


function CalenderOnChange() {
    //alert(x.value);
    //alert("CalenderOnChange");
    var startDate = document.getElementById("schStartdate").value;
    //alert("date"+startDate);

    var id = document.getElementById("scheduleid").value;
    //document.getElementById("load").style.display='block';

    var req = getXMLHttpRequest();
    document.getElementById("scheduleid").value = id;
    req.onreadystatechange = readyStateHandlerString(req, populateOverlayDetails);

    //alert("step1");
    var url = "../ajax/getReportOverlayDetails.action?&id=" + id + "&startDate=" + startDate;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}
//function for checking SENDER_ITEM and RECEIVER_ITEM for codelist

var itemcount;
function checkItems(count) {
    itemcount = count;
    document.getElementById("loadingAcoountSearch").style.display = "block";
    var selectedCodeList = document.getElementById("listName").value;
    var rowCount = $('#results tr').length;
    for (i = 1; i < rowCount; i++) {
        var flag = checkArray(i);
        if (flag == true)
        {
            continue;
        } else if (flag == false) {
            if (i != count) {
                if ((document.getElementById("senderItem" + i).value == document.getElementById("senderItem" + count).value) && (document.getElementById("recItem" + i).value == document.getElementById("recItem" + count).value))
                {
                    alert("Sender Code and Receiver Code already entered. Please try with different one.");
                    document.getElementById("senderItem" + count).value = "";
                    document.getElementById("recItem" + count).value = "";
                    window.setTimeout(function () {
                        // This will execute 5 seconds later
                        document.getElementById('messagediv').innerHTML = "";

                    }, 5000);
                }
            }
        }

    }
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, result);


    var url = "../ajax/searchItems.action?&senderItem=" + document.getElementById("senderItem" + count).value + "&recItem=" + document.getElementById("recItem" + count).value + "&selectedName=" + selectedCodeList;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}


function result(responseText) {
    if (responseText == "Failure")
    {

        document.getElementById("senderItem" + itemcount).value = "";
        document.getElementById("recItem" + itemcount).value = "";
        alert("Sender Code and Receiver Code already exists. Please try with different one.  ");
        window.setTimeout(function () {
            // This will execute 5 seconds later
            document.getElementById('messagediv').innerHTML = "";

        }, 5000);
    }
}



/*
 function checkListName() {
 document.getElementById("loadingAcoountSearch").style.display = "block";
 var newCodeListName=document.getElementById("newname").value ;
 var req = getXMLHttpRequest();
 req.onreadystatechange = readyStateHandlerLoadText2(req, result1);
 
 
 var url = "../ajax/checkCodeListName.action?&newListName=" + newCodeListName;
 req.open("GET", url, "true");
 req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
 req.send(null);
 }
 
 
 function result1(responseText){
 if (responseText == "Failure")
 {
 document.getElementById("newname").value = "";
 alert("Code List name already exists. Please try with different one");
 }
 }
 */
// function to check whether deleted row from codelist grid exists in array or not
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

function getInventoryDetails(number, id, db) {
    var num = number;
    // var ponum = ponum;
    var id = id;
    $(function () {
        $('#detail_box').show();
        return false;
    });
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateInventoryDetails);
    var url = "../ajax/getInventoryDetails.action?fileId=" + num + "&id=" + id + "&database=" + db;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}



function populateInventoryDetails(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    if (chk.childNodes[0].nodeValue == "true") {
        document.getElementById('errorDiv').value = "";
        var detail = details.childNodes[0];

        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var docType = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;

        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;

        var SENDER_ID = detail.getElementsByTagName("SENDERID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVERID")[0].childNodes[0].nodeValue;
        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        // var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var REFERENCE_NUMBER = detail.getElementsByTagName("REFERENCE_NUMBER")[0].childNodes[0].nodeValue;
        var PRI_KEY_TYPE = detail.getElementsByTagName("PRI_KEY_TYPE")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILEID")[0].childNodes[0].nodeValue;
        var ERRMESSAGE = detail.getElementsByTagName("ERR_MESSAGE")[0].childNodes[0].nodeValue;
        var TRANSACTION_TYPE = detail.getElementsByTagName("TRANSACTION_TYPE")[0].childNodes[0].nodeValue;
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var ITEMS_COUNT = detail.getElementsByTagName("ITEMS_COUNT")[0].childNodes[0].nodeValue;

        document.getElementById('ManFileId').value = fileid;
        document.getElementById('Manpri_key_type').value = PRI_KEY_TYPE;
        document.getElementById('Manpri_key_value').value = REFERENCE_NUMBER;
        if (SENDER_NAME == "NULL") {
            SENDER_NAME = "Null";
        }
        if (RECEIVER_NAME == "NULL") {
            RECEIVER_NAME = "Null";
        }
        document.getElementById('ManDocumentType').value = docType;
        document.getElementById('ManTransactionType').value = TRANSACTION_TYPE;
        document.getElementById('ManSenderId').value = SENDER_ID;
        document.getElementById('ManSenderName').value = SENDER_NAME;
        document.getElementById('ManReceiverId').value = RECEIVER_ID;
        document.getElementById('ManReceiverName').value = RECEIVER_NAME;
        document.getElementById('ManISA').value = ISA_NUMBER;
        document.getElementById('ManGs').value = GS_CONTROL_NUMBER;
        // document.getElementById('ManSt').value = ST_CONTROL_NUMBER;
        document.getElementById('ManIsADate').value = ISA_DATE;
        document.getElementById('ManIsATime').value = ISA_TIME;

        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('ManStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('ManStatus').value = STATUS;
        } else {
            document.getElementById('ManStatus').value = STATUS;
        }
        document.getElementById('itemCount').value = ITEMS_COUNT;
        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('ManPreTranslation').innerHTML = "--";
        } else {
            document.getElementById('ManPreTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('ManPostTranslation').innerHTML = "--";
        } else {
            document.getElementById('ManPostTranslation').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ACKFILEID == "No File") {
            document.getElementById('ManAckFileId').innerHTML = "--";
        } else {
            document.getElementById('ManAckFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        if (ERRMESSAGE != "NO MSG") {
            document.getElementById('errorDiv').style.display = "block";
            document.getElementById('errorReportDiv').style.display = "block";
            document.getElementById('InvErrormessage').innerHTML = ERRMESSAGE;
        } else {
            document.getElementById('InvErrormessage').innerHTML = "--";
        }


        document.getElementById('ManNullValues').innerHTML = "<a style=\"color: deepskyblue\" href=\"javascript:getNullValues('<%=docRepositoryBean.getId()%>');\">Dispalay Null</a></td></tr>";
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";
    }
    $('#hide-menu1').addClass('show-menu');
}



function getTransactionsBasedOnType() {
    var type = document.getElementById("businessFlows").value;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerXml(req, getTransactionsBasedOnTypeResponse);
    var url = "../ajax/getTransactionsBasedOnType.action?flag=" + type;
    req.open("GET", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function getTransactionsBasedOnTypeResponse(resXML) {
    var docType = document.getElementById("docType");
    var transactions = resXML.getElementsByTagName("TRANSACTIONS")[0];
    var transactionTypes = transactions.getElementsByTagName("TRANSACTION_TYPE");
    docType.innerHTML = " ";
    for (var i = 0; i < transactionTypes.length; i++) {
        var transactionType = transactionTypes[i];
        var att = transactionType.getAttribute("transaction");
        var name = transactionType.textContent;
        var opt = document.createElement("option");
        opt.setAttribute("value", att);
        opt.appendChild(document.createTextNode(name));
        docType.appendChild(opt);
    }

}


function readyStateHandlerXml(req, responseXmlHandler) {
    return function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                responseXmlHandler(req.responseXML);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        }
    }
}

function getPartnerAndDocType() {
    var type = document.getElementById("businessFlows").value;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerXml(req, getTransactionsAndDocTypeResponse);
    var url = "../ajax/getPartnerAndDocType.action?flag=" + type;
    req.open("GET", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}

function getTransactionsAndDocTypeResponse(resXML) {
    var tpPartnerId = document.getElementById("tpPartnerId");
    var partners = resXML.getElementsByTagName("PARTNER")[0];
    var partnernames = partners.getElementsByTagName("NAME");
    tpPartnerId.innerHTML = " ";
    for (var i = 0; i < partnernames.length; i++) {
        var partnername = partnernames[i];
        //  var attr = partnername.getAttribute("partner");
        var attr = partnername.getAttribute("id");
        var name = partnername.firstChild.nodeValue;
        var opt1 = document.createElement("option");
        opt1.setAttribute("value", attr);
        opt1.appendChild(document.createTextNode(name));
        tpPartnerId.appendChild(opt1);
    }

    var docType = document.getElementById("docType");
    var transactions = resXML.getElementsByTagName("TRANSACTIONS")[0];
    var transactionTypes = transactions.getElementsByTagName("TRANSACTION_TYPE");
    docType.innerHTML = " ";
    for (var i = 0; i < transactionTypes.length; i++) {
        var transactionType = transactionTypes[i];
        var att = transactionType.getAttribute("transaction");
        var name = transactionType.firstChild.nodeValue;
        var opt = document.createElement("option");
        opt.setAttribute("value", att);
        opt.appendChild(document.createTextNode(name));
        docType.appendChild(opt);
    }

}


function getTransactionReferences(instanceId, transactionType, senderId, receiverId) {
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateTransactionReferences);
    var url = "../ajax/getTransactionReferences.action?docType=" + transactionType + "&fileId=" + instanceId + "&senderId=" + senderId + "&receiverId=" + receiverId;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateTransactionReferences(responseXML) {
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    if (chk.childNodes[0].nodeValue == "true") {
        var detail = details.childNodes[0];
        var DOC_TYPE = detail.getElementsByTagName("DOC_TYPE")[0].childNodes[0].nodeValue;
        document.getElementById('docType').value = DOC_TYPE;

        var ACK_STATUS = detail.getElementsByTagName("ACK_STATUS")[0].childNodes[0].nodeValue;
        var PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
        var IDOC_NUM = detail.getElementsByTagName("IDOC_NUM")[0].childNodes[0].nodeValue;
        var DATE_TIME_RECEIVED = detail.getElementsByTagName("DATE_TIME_RECEIVED")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRE_TRANS_FILEPATH")[0].childNodes[0].nodeValue;
        PRE_TRANS_FILEPATH = PRE_TRANS_FILEPATH.substring(PRE_TRANS_FILEPATH.lastIndexOf('/') + 1);
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POST_TRANS_FILEPATH")[0].childNodes[0].nodeValue;
        POST_TRANS_FILEPATH = POST_TRANS_FILEPATH.substring(POST_TRANS_FILEPATH.lastIndexOf('/') + 1);
        var STATE = detail.getElementsByTagName("STATE")[0].childNodes[0].nodeValue;

        //file links

        var PRE_TRANS_FILEPATH_COMPLETE = detail.getElementsByTagName("PRE_TRANS_FILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH_COMPLETE = detail.getElementsByTagName("POST_TRANS_FILEPATH")[0].childNodes[0].nodeValue;

        document.getElementById('preTransalationPath').innerHTML = PRE_TRANS_FILEPATH_COMPLETE;
        document.getElementById('postTransalationPath').innerHTML = POST_TRANS_FILEPATH_COMPLETE;

        //file link end

        if (DOC_TYPE == '856' || DOC_TYPE == 'DESADV') {
            var BOL_NUMBER = detail.getElementsByTagName("BOL_NUMBER")[0].childNodes[0].nodeValue;
            var ASN_NUMBER = detail.getElementsByTagName("ASN_NUMBER")[0].childNodes[0].nodeValue;
            document.getElementById('BillOfLadingValue').innerHTML = BOL_NUMBER;
            document.getElementById('ShipmentIDValue').innerHTML = ASN_NUMBER;
        }

        if (DOC_TYPE == '810' || DOC_TYPE == 'INVOIC') {
            var INVOICE_NUMBER = detail.getElementsByTagName("INVOICE_NUMBER")[0].childNodes[0].nodeValue;
            document.getElementById('InvoiceNumberValue').innerHTML = INVOICE_NUMBER;
        }

        document.getElementById('AckStatusValue').innerHTML = ACK_STATUS;
        document.getElementById('PurchaseOrderNumberValue').innerHTML = PO_NUMBER;
        document.getElementById('OpenTextArchiveEDICommFileValue').innerHTML = PRE_TRANS_FILEPATH;
        document.getElementById('OpenTextArchiveMetaDataFileValue').innerHTML = POST_TRANS_FILEPATH;
        document.getElementById('IDocNumberValue').innerHTML = IDOC_NUM;
//        var date=DATE_TIME_RECEIVED.substring(0,16);
//        alert(date);
//        document.getElementById('date1').innerHTML = date;
        document.getElementById('State').innerHTML = STATE;
        var date = DATE_TIME_RECEIVED.substring(0, 16);
        document.getElementById('date1').innerHTML = date;
        document.getElementById('date2').innerHTML = date;
        document.getElementById('date3').innerHTML = date;
        document.getElementById('date4').innerHTML = date;
        document.getElementById('date5').innerHTML = date;
        document.getElementById('date6').innerHTML = date;
        document.getElementById('date7').innerHTML = date;
        document.getElementById('date8').innerHTML = date;
        document.getElementById('date9').innerHTML = date;

        $('#selfReg').modal();

    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";
    }
    $('#hide-menu1').addClass('show-menu');
}

function preTranslationtextDataView() {
    var myWindow;
    var x = document.getElementById("preTransalationPath").innerText
    var req = getXMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {

            //   alert(this.responseText);
            myWindow = window.open("", "myWindow", "width=650,height=400");
            myWindow.document.write("<span><pre>" + this.responseText + "</span>");
        }
    }
    var url = "../ajax/getPrePosttranslationPathData.action?prePostTranslationpath=" + x;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function postTranslationtextDataView() {
    var myWindow;
    var x = document.getElementById("postTransalationPath").innerText;
    var req = getXMLHttpRequest();
    req.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {

            //  alert(this.responseText);

            myWindow = window.open("", "myWindow", "width=650,height=400");
            myWindow.document.write("<span><pre>" + this.responseText + "</span>");

        }
    }
    var url = "../ajax/getPrePosttranslationPathData.action?prePostTranslationpath=" + x;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

//For time interval

function changeTimeinterval() {
    var timeInterval = document.forms["documentForm"]["timeInterval"].value;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, populatechangeTimeinterval);
    var url = "../ajax/appConfigEditTimeinterval.action?timeInterval=" + timeInterval;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populatechangeTimeinterval(responseText) {
    alert(responseText);
}


function changeTop10EDITP() {
    var ediTP1 = document.forms["topEDITP"]["ediTP1"].value;
    if ((ediTP1 != "-1") && (ediTP1 == "")) {
        alert("Please enter TradingPartner1 !!");
        return false;
    }
    var ediTP2 = document.forms["topEDITP"]["ediTP2"].value;
    if ((ediTP2 != "-1") && (ediTP2 == "")) {
        alert("Please enter TradingPartner2 !!");
        return false;
    }
    var ediTP3 = document.forms["topEDITP"]["ediTP3"].value;
    if ((ediTP3 != "-1") && (ediTP3 == "")) {
        alert("Please enter TradingPartner3 !!");
        return false;
    }
    var ediTP4 = document.forms["topEDITP"]["ediTP4"].value;
    if ((ediTP4 != "-1") && (ediTP4 == "")) {
        alert("Please enter TradingPartner4 !!");
        return false;
    }
    var ediTP5 = document.forms["topEDITP"]["ediTP5"].value;
    if ((ediTP5 != "-1") && (ediTP5 == "")) {
        alert("Please enter TradingPartner5 !!");
        return false;
    }
    var ediTP6 = document.forms["topEDITP"]["ediTP6"].value;
    if ((ediTP6 != "-1") && (ediTP6 == "")) {
        alert("Please enter TradingPartner6 !!");
        return false;
    }
    var ediTP7 = document.forms["topEDITP"]["ediTP7"].value;
    if ((ediTP7 != "-1") && (ediTP7 == "")) {
        alert("Please enter TradingPartner7 !!");
        return false;
    }
    var ediTP8 = document.forms["topEDITP"]["ediTP8"].value;
    if ((ediTP8 != "-1") && (ediTP8 == "")) {
        alert("Please enter TradingPartner8 !!");
        return false;
    }
    var ediTP9 = document.forms["topEDITP"]["ediTP9"].value;
    if ((ediTP9 != "-1") && (ediTP9 == "")) {
        alert("Please enter TradingPartner9 !!");
        return false;
    }
    var ediTP10 = document.forms["topEDITP"]["ediTP10"].value;
    if ((ediTP10 != "-1") && (ediTP10 == "")) {
        alert("Please enter TradingPartner10 !!");
        return false;
    }
    var allEDItp = ediTP1 + "," + ediTP2 + "," + ediTP3 + "," + ediTP4 + "," + ediTP5 + "," + ediTP6 + "," + ediTP7 + "," + ediTP8 + "," + ediTP9 + "," + ediTP10;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, populatechangeTop10EDITP);
    var url = "../ajax/appConfigEditTop10EDITP.action?allEDItp=" + allEDItp;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populatechangeTop10EDITP(responseText) {
// $('#top10EDITPModal').modal('hide');
    alert(responseText);
}

function changeTop10RailTP() {
    var railTP1 = document.forms["topRailTP"]["railTP1"].value;
    if ((railTP1 != "-1") && (railTP1 == "")) {
        alert("Please enter TradingPartner1 !!");
        return false;
    }
    var railTP2 = document.forms["topRailTP"]["railTP2"].value;
    if ((railTP2 != "-1") && (railTP2 == "")) {
        alert("Please enter TradingPartner2 !!");
        return false;
    }
    var railTP3 = document.forms["topRailTP"]["railTP3"].value;
    if ((railTP3 != "-1") && (railTP3 == "")) {
        alert("Please enter TradingPartner3 !!");
        return false;
    }
    var railTP4 = document.forms["topRailTP"]["railTP4"].value;
    if ((railTP4 != "-1") && (railTP4 == "")) {
        alert("Please enter TradingPartner4 !!");
        return false;
    }
    var railTP5 = document.forms["topRailTP"]["railTP5"].value;
    if ((railTP5 != "-1") && (railTP5 == "")) {
        alert("Please enter TradingPartner5 !!");
        return false;
    }
    var railTP6 = document.forms["topRailTP"]["railTP6"].value;
    if ((railTP6 != "-1") && (railTP6 == "")) {
        alert("Please enter TradingPartner6 !!");
        return false;
    }
    var railTP7 = document.forms["topRailTP"]["railTP7"].value;
    if ((railTP7 != "-1") && (railTP7 == "")) {
        alert("Please enter TradingPartner7 !!");
        return false;
    }
    var railTP8 = document.forms["topRailTP"]["railTP8"].value;
    if ((railTP8 != "-1") && (railTP8 == "")) {
        alert("Please enter TradingPartner8 !!");
        return false;
    }
    var railTP9 = document.forms["topRailTP"]["railTP9"].value;
    if ((railTP9 != "-1") && (railTP9 == "")) {
        alert("Please enter TradingPartner9 !!");
        return false;
    }
    var railTP10 = document.forms["topRailTP"]["railTP10"].value;
    if ((railTP10 != "-1") && (railTP10 == "")) {
        alert("Please enter TradingPartner10 !!");
        return false;
    }
    var allRailtp = railTP1 + "," + railTP2 + "," + railTP3 + "," + railTP4 + "," + railTP5 + "," + railTP6 + "," + railTP7 + "," + railTP8 + "," + railTP9 + "," + railTP10;
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, populatechangeTop10RailTP);
    var url = "../ajax/appConfigEditTop10RailTP.action?allRailtp=" + allRailtp;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populatechangeTop10RailTP(responseText) {
// $('#top10EDITPModal').modal('hide');
    alert(responseText);
}

function changeScvpDbValues() {
    var dbinfo = {"jsonData": []};
    for (var i = 0; i < dbcount; i++) {
        var dbPropertyName = document.getElementById('dbPropertyName' + i).value;
        if ((dbPropertyName != "-1") && (dbPropertyName == "")) {
            document.getElementById("bmcResultMessage").innerHTML = "<font color='red'>Please enter DB property name!!</font>";
            return false;
        }
        var dbPropertyValue = document.getElementById('dbPropertyValue' + i).value;
        if ((dbPropertyValue != "-1") && (dbPropertyValue == "")) {
            document.getElementById("bmcResultMessage").innerHTML = "<font color='red'>Please enter DB property value!!</font>";
            return false;
        }
        var dbDisplayOrder = document.getElementById('dbDisplayOrder' + i).value;
        if ((dbDisplayOrder != "-1") && (dbDisplayOrder == "")) {
            document.getElementById("bmcResultMessage").innerHTML = "<font color='red'>Please enter DB display order!!</font>";
            return false;
        }
        dbinfo["jsonData"].push({
            "dbPropertyName": document.getElementById('dbPropertyName' + i).value,
            "dbPropertyValue": document.getElementById('dbPropertyValue' + i).value,
            "dbDisplayOrder": document.getElementById('dbDisplayOrder' + i).value
        });

    }
    var dbArray = JSON.stringify(dbinfo["jsonData"]);
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, populateScvpDBValues);
    var url = "../ajax/appConfigEditScvpDatabaseValues.action?dbData=" + dbArray;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateScvpDBValues(responseText) {
    alert(responseText);
}

function changeTransactionNames() {

    var ediTransactionNamesList = document.forms["transactionsForm"]["ediTransactionNamesList"].value;
    if ((ediTransactionNamesList != "-1") && (ediTransactionNamesList == "")) {
        alert("Please enter EDI Transactions with comma separated!!");
        return false;
    }
    var railTransactionNamesList = document.forms["transactionsForm"]["railTransactionNamesList"].value;
    if ((railTransactionNamesList != "-1") && (railTransactionNamesList == "")) {
        alert("Please enter RAIL Transactions with comma separated!!");
        return false;
    }
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerLoadText2(req, populateTransactionNames);
    var url = "../ajax/appConfigEditTransactions.action?ediTransactionNamesList=" + ediTransactionNamesList + "&railTransactionNamesList=" + railTransactionNamesList;
    req.open("GET", url, "true");
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(null);
}

function populateTransactionNames(responseText) {
    document.getElementById("TransactionNamesResultMessage").innerHTML = "<font color='blue'>" + responseText + "</font>";
    window.location = "../appConfigurations/getAppConfigurations.action";
}