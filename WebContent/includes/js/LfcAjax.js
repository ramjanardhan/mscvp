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

function readyStateHandlerText(req, responseTextHandler) {
    return function() {
        if (req.readyState == 4) {
            if (req.status == 200) {
                responseTextHandler(req.responseXML);
            } else {
                alert("HTTP error" + req.status + " : " + req.statusText);
            }
        }

    }
}

/**
 * For doc Ajax call
 * 
 */
function getlfcPODetails(number, id, type, db) {
    //alert(db);
    var num = number;
    var id = id;
    var type = type;
    $(function() {
        $('#detail_box').show();
        return false;
    });
    // alert("in js--->"+num+"-----"+type);
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLifecycle);
    var url = "../ajax/LifecycleDetails.action?poNumber=" + num + "&fileId=" + id + "&type=" + type + "&database=" + db;
    req.open("GET", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}



function populateLifecycle(responseXML){
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];

    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;
        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        var ACKFILEID = detail.getElementsByTagName("ACKFILE")[0].childNodes[0].nodeValue;
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;

        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        var PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;
//        var PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
//        var PO_STATUS = detail.getElementsByTagName("PO_STATUS")[0].childNodes[0].nodeValue;
//        var SO_NUMBER = detail.getElementsByTagName("SO_NUMBER")[0].childNodes[0].nodeValue;
//        var ITEM_QTY = detail.getElementsByTagName("ITEM_QTY")[0].childNodes[0].nodeValue;

        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        var TRANS_TYPE = detail.getElementsByTagName("TRANS_TYPE")[0].childNodes[0].nodeValue;
        var GS_CONTROL_NUMBER = detail.getElementsByTagName("GS_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var ST_CONTROL_NUMBER = detail.getElementsByTagName("ST_CONTROL_NUMBER")[0].childNodes[0].nodeValue;
        var PRI_KEY_TYPE = detail.getElementsByTagName("PRI_KEY_TYPE")[0].childNodes[0].nodeValue;
        var PRI_KEY_Val = detail.getElementsByTagName("PRI_KEY_VAL")[0].childNodes[0].nodeValue;
        
        document.getElementById('LfcInstanceId').value = fileid;
        document.getElementById('LfcPONum').value = PO_NUMBER;
          document.getElementById('LfcManpri_key_type').value = PRI_KEY_TYPE;
            document.getElementById('LfcManpri_key_value').value = PRI_KEY_Val;
        //document.getElementById('LfcPONum').value = "--";
//        document.getElementById('LfcPODates').value = PO_DATE;
//        document.getElementById('LfcPODates').value = "--";
//        document.getElementById('LfcStatus1').value = PO_STATUS;
//        document.getElementById('LfcStatus1').value = "--";
//        document.getElementById('LfcSo').value = SO_NUMBER;
//        document.getElementById('LfcSo').value = "--";
//        document.getElementById('LfcPOQty').value = ITEM_QTY;
        //document.getElementById('LfcPOQty').value = "--";
        document.getElementById('LfcTransactionType').value = TRANS_TYPE;
        //document.getElementById('LfcTransactionType').value = "--";
        document.getElementById('LycPOPartnerId').value = SENDER_ID;
        document.getElementById('LycPOPartnerName').value = SENDER_NAME;
        document.getElementById('LfcPOReceiverId').value = RECEIVER_ID;
        document.getElementById('LfcPOReceiverName').value = RECEIVER_NAME;
        document.getElementById('LfcPOIsa').value = ISA_NUMBER;
        document.getElementById('LfcGS_CONTROL_NUMBER').value =GS_CONTROL_NUMBER;
        document.getElementById('LfcST_CONTROL_NUMBER').value =ST_CONTROL_NUMBER;
        document.getElementById('LfcPOISADate').value = ISA_DATE;
        //document.getElementById('LfcPOISADate').value = "--";
        document.getElementById('LfcPOIsATime').value = ISA_TIME;
        //document.getElementById('LfcPOIsATime').value = "--";
        
        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('LfcPOStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('LfcPOStatus').value = STATUS;
        } else {
            document.getElementById('LfcPOStatus').value = STATUS;
        }

        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('LfcPOPreTransition').innerHTML = "--";
        } else {
            document.getElementById('LfcPOPreTransition').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('LfcPOPostTransition').innerHTML = "--";

        } else {
            document.getElementById('LfcPOPostTransition').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('LfcPOAckFileId').innerHTML = "--";
        } else {
            document.getElementById('LfcPOAckFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        // this value is not coming so skipped to show detail Info. Please check in XML response and check the ID of error msg.

//       if(ERRMESSAGE != "NO MSG"){
//            document.getElementById('InvErrormessage').innerHTML=ERRMESSAGE;
//               
//        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }

    $('#hide-menu1').addClass('show-menu');


}

function getLtlfcPODetails(number, id, type) {
    var num = number;
    var id = id;
    var type = type;
    var database = document.getElementById("database").value;
    $(function() {

        $('#detail_box').show();
        return false;

    });
    // alert("in js--->"+num+"-----"+type);
    var req = getXMLHttpRequest();
    req.onreadystatechange = readyStateHandlerText(req, populateLtLifecycle);
    var url = "../ajax/ltLifecycleDetails.action?poNumber=" + num + "&fileId=" + id + "&type=" + type+ "&database="+database;
    req.open("GET", url, "true");
    // req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(null);
}
function populateLtLifecycle(responseXML)
{
    var details = responseXML.getElementsByTagName("DETAILS")[0];
    var detail = details.childNodes[0];
    var chk = detail.getElementsByTagName("VALID")[0];
    if (chk.childNodes[0].nodeValue == "true") {

        var detail = details.childNodes[0];
        var fileid = detail.getElementsByTagName("FILEID")[0].childNodes[0].nodeValue;
            var filetype = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue;
        var shipmentid = detail.getElementsByTagName("SHIPMENTID")[0].childNodes[0].nodeValue;
   
   var st_ctrl_num = detail.getElementsByTagName("STCTRLNUM")[0].childNodes[0].nodeValue;
        var gs_ctrl_num = detail.getElementsByTagName("GSCTRLNUM")[0].childNodes[0].nodeValue;
        //  var docType = detail.getElementsByTagName("FILETYPE")[0].childNodes[0].nodeValue; 
        var PRE_TRANS_FILEPATH = detail.getElementsByTagName("PRETRANSFILEPATH")[0].childNodes[0].nodeValue;

        var POST_TRANS_FILEPATH = detail.getElementsByTagName("POSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        //var Status = detail.getElementsByTagName("S")[0].childNodes[0].nodeValue; 
        var ACKFILEID = detail.getElementsByTagName("ACKFILE")[0].childNodes[0].nodeValue;
        if (ACKFILEID == "null") {
            ACKFILEID = "--"
        }
        var SENDER_ID = detail.getElementsByTagName("SENDER_ID")[0].childNodes[0].nodeValue;

        if (SENDER_ID == "null") {
            SENDER_ID = "--"
        }
        var RECEIVER_ID = detail.getElementsByTagName("RECEIVER_ID")[0].childNodes[0].nodeValue;

        if (RECEIVER_ID == "null") {
            RECEIVER_ID = "--"
        }


        var SENDER_NAME = detail.getElementsByTagName("SENDER_NAME")[0].childNodes[0].nodeValue;
        if (SENDER_NAME == "null") {
            SENDER_NAME = "--"
        }


        var RECEIVER_NAME = detail.getElementsByTagName("RECEIVER_NAME")[0].childNodes[0].nodeValue;

        if (RECEIVER_NAME == "null") {
            RECEIVER_NAME = "--"
        }

        var TRAN_NUMBER = detail.getElementsByTagName("TRAN_NUMBER")[0].childNodes[0].nodeValue;
        if (TRAN_NUMBER == "null") {
            TRAN_NUMBER = "--"
        }

        //  var ORG_FILEPATH = detail.getElementsByTagName("ORGFILEPATH")[0].childNodes[0].nodeValue; 

        var dttm = detail.getElementsByTagName("DATETIME")[0].childNodes[0].nodeValue;

        if (dttm == "null") {
            dttm = "--"
        }
        var STATUS = detail.getElementsByTagName("STATUS")[0].childNodes[0].nodeValue;
        if (STATUS == "null") {
            STATUS = "--"
        }
        /*
         * 
         * newly Added
         */
        var PO_NUMBER = detail.getElementsByTagName("PO_NUMBER")[0].childNodes[0].nodeValue;

        if (PO_NUMBER == "null") {
            PO_NUMBER = "--"
        }
        // var PO_DATE = detail.getElementsByTagName("PO_DATE")[0].childNodes[0].nodeValue;
        //  var PO_STATUS = detail.getElementsByTagName("PO_STATUS")[0].childNodes[0].nodeValue;
        //  var SO_NUMBER = detail.getElementsByTagName("SO_NUMBER")[0].childNodes[0].nodeValue;
        //  var SAPIDOC_NUMBER = detail.getElementsByTagName("SAPIDOC_NUMBER")[0].childNodes[0].nodeValue;
        //  var ITEM_QTY = detail.getElementsByTagName("ITEM_QTY")[0].childNodes[0].nodeValue;

        //   var ASN_NUMBER = detail.getElementsByTagName("ASN_NUMBER")[0].childNodes[0].nodeValue;
        //  var BOL_NUMBER = detail.getElementsByTagName("BOL_NUMBER")[0].childNodes[0].nodeValue;

        //  if(BOL_NUMBER=="null"){
        // BOL_NUMBER="--" 
        //}
        var ISA_NUMBER = detail.getElementsByTagName("ISA_NUMBER")[0].childNodes[0].nodeValue;
        if (ISA_NUMBER == "null") {
            ISA_NUMBER = "--"
        }
        var ISA_DATE = detail.getElementsByTagName("ISA_DATE")[0].childNodes[0].nodeValue;
        if (ISA_DATE == "null") {
            ISA_DATE = "--"
        }

        var ISA_TIME = detail.getElementsByTagName("ISA_TIME")[0].childNodes[0].nodeValue;
        if (ISA_TIME == "null") {
            ISA_TIME = "--"
        }

        //  var INV_NUMBER = detail.getElementsByTagName("INV_NUMBER")[0].childNodes[0].nodeValue;
        // var INV_AMOUNT = detail.getElementsByTagName("INV_AMOUNT")[0].childNodes[0].nodeValue;
        // var CHEQUE_NUMBER = detail.getElementsByTagName("CHEQUE_NUMBER")[0].childNodes[0].nodeValue;
        var TRANS_TYPE = detail.getElementsByTagName("TRANS_TYPE")[0].childNodes[0].nodeValue;
        var DIRECTION = detail.getElementsByTagName("DIRECTION")[0].childNodes[0].nodeValue;

        if (DIRECTION == "null") {
            DIRECTION = "--"
        }
        // if(TRANS_TYPE=='204'){
        //   var COMMENTS = detail.getElementsByTagName("COMMENTS")[0].childNodes[0].nodeValue;
        // var MODPOSTTRANSFILEPATH = detail.getElementsByTagName("MODPOSTTRANSFILEPATH")[0].childNodes[0].nodeValue;
        //var MODFLAG = detail.getElementsByTagName("MODFLAG")[0].childNodes[0].nodeValue;
        //}    

        var ERRORMSG = detail.getElementsByTagName("ERRORMSG")[0].childNodes[0].nodeValue;


        document.getElementById('LfcInstanceId').value = fileid;
//        if (PO_NUMBER != "NO") {
//            document.getElementById('LfcPONum').value = PO_NUMBER;
//        }
//        else
//        {
//            document.getElementById('LfcPONum').value = "--";
//        }
        document.getElementById('LfcShipment').value = shipmentid;
		document.getElementById('LfcDocumentType').value = filetype;
//        if(PO_DATE != "NO"){
//            document.getElementById('LfcPODates').value=PO_DATE;
//        }
//        else
//        {
//            document.getElementById('LfcPODates').value="--";
//        }
//        if(PO_STATUS != "NO"){
//            document.getElementById('LfcStatus1').value=PO_STATUS;
//        }    
//        else{
//            document.getElementById('LfcStatus1').value="--";
//        }
//        if(SO_NUMBER != "NO"){
//            document.getElementById('LfcSo').value=SO_NUMBER;
//        }  
//        else{
//            document.getElementById('LfcSo').value="--";
//        }
//        if(ITEM_QTY  != "NO"){
//            document.getElementById('LfcPOQty').value=ITEM_QTY ;
//        }
//        else
//        {
//            document.getElementById('LfcPOQty').value="--";
//        }
        if (TRANS_TYPE != "NO") {
            document.getElementById('LfcTransactionType').value = TRANS_TYPE;
        }
        else
        {
            document.getElementById('LfcTransactionType').value = "--";
        }
        document.getElementById('LycPOPartnerId').value = SENDER_ID;
        document.getElementById('LycPOPartnerName').value = SENDER_NAME;
        document.getElementById('LfcPOReceiverId').value = RECEIVER_ID;
        document.getElementById('LfcPOReceiverName').value = RECEIVER_NAME;
        if (ISA_NUMBER != 0) {
            document.getElementById('LfcPOIsa').value = ISA_NUMBER;
        }
        else {
            document.getElementById('LfcPOIsa').value = "--";
        }
        if (ISA_DATE != null) {
            document.getElementById('LfcPOISADate').value = ISA_DATE;
        }
        else {
            document.getElementById('LfcPOISADate').value = "--";
        }
            if (st_ctrl_num != null) {
            document.getElementById('LfccSt').value = st_ctrl_num;
        }
        else {
            document.getElementById('LfccSt').value = "--";
        }
            if (gs_ctrl_num != null) {
            document.getElementById('LofcGs').value = gs_ctrl_num;
        }
        else {
            document.getElementById('LofcGs').value = "--";
        }
        if (ISA_TIME != 0) {
            document.getElementById('LfcPOIsATime').value = ISA_TIME;
        }
        else
        {
            document.getElementById('LfcPOIsATime').value = "--";
        }

        if (STATUS.toUpperCase() == "ERROR") {
            document.getElementById('LfcPOStatus').value = STATUS;
        } else if (STATUS.toUpperCase() == "SUCCESS") {
            document.getElementById('LfcPOStatus').value = STATUS;
        } else {
            document.getElementById('LfcPOStatus').value = STATUS;
        }

        if (PRE_TRANS_FILEPATH == "No File") {
            document.getElementById('LfcPOPreTransition').innerHTML = "--";
        } else {
            document.getElementById('LfcPOPreTransition').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + PRE_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (POST_TRANS_FILEPATH == "No File") {
            document.getElementById('LfcPOPostTransition').innerHTML = "--";

        } else {
            document.getElementById('LfcPOPostTransition').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + POST_TRANS_FILEPATH.replace(/\\/g,"*") + "\">Download</a>";
        }
        if (ACKFILEID == "No File") {
            document.getElementById('LfcPOAckFileId').innerHTML = "--";
        } else {
            document.getElementById('LfcPOAckFileId').innerHTML = "<a style=\"color: deepskyblue\" href=\"../download/getAttachment.action?locationAvailable=" + ACKFILEID.replace(/\\/g,"*") + "\">Download</a>";
        }

        // this value is not coming so skipped to show detail Info. Please check in XML response and check the ID of error msg.

//       if(ERRMESSAGE != "NO MSG"){
//            document.getElementById('InvErrormessage').innerHTML=ERRMESSAGE;
//               
//        }
    }
    if (chk.childNodes[0].nodeValue == "false") {
        document.getElementById('noresult').value = " <h5 >Sorry ! No Results Found</h5>";


    }
    $('#hide-menu1').addClass('show-menu');


}


