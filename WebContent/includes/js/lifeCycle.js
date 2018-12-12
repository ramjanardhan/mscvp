/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//for life cycle
 function getLifeCycle(checkList,flow,db) {
//     var db = document.getElementById('database').value;
   // alert(db);
     if(flow=="manufacturing"){
                var res = false;
                var i = 0;
                var po_Num = "";
                for (var j = 0; j < checkList; j++) {
                    if (document.getElementById("check_List" + j) != null) {
                        if (checkList == 1)
                        {
                            res = document.getElementById("check_List" + j).checked;

                        } else
                        {
                            res = document.getElementById("check_List" + j).checked;
                        }

                        if (res == true) {
                            i = i + 1;
                            if (i == 1) {
                                po_Num = po_Num + document.getElementById("text" + j).value;
                            }
                        }
                    }
                }
                if (i != 1) {
                    alert("Please select one checkbox to get LifeCycle of PO !!");
                    return false;
                }
                else {
                    //alert(po_Num);
                    //window.location = "../lfc/lifeCycle.action?poNumber="+po_Num; 
                    //purchaseForm.action = "../lfc/lifeCycle.action?poNumber="+po_Num;
                    location.href = "../lfc/lifecycle.action?poNumber=" + po_Num+ "&database=" + db;
                    return true;
                }
            }
            else if(flow=="logistics")
            {
                var res = false;
                var i = 0;
                var shipment_Num = "";
                for (var j = 0; j < checkList; j++) {
                    if (document.getElementById("check_List" + j) != null) {
                        if (checkList == 1)
                        {
                            res = document.getElementById("check_List" + j).checked;

                        } else
                        {
                            res = document.getElementById("check_List" + j).checked;
                        }

                        if (res == true) {
                            i = i + 1;
                            if (i == 1) {
                                shipment_Num = shipment_Num + document.getElementById("text" + j).value;
                            }
                        }
                    }
                }
                if (i != 1) {
                    alert("Please select one checkbox to get LifeCycle of PO !!");
                    return false;
                }
                else {
                    //alert(po_Num);
                    //window.location = "../lfc/lifeCycle.action?poNumber="+po_Num; 
                    //purchaseForm.action = "../lfc/lifeCycle.action?poNumber="+po_Num;
                    location.href = "../lfc/ltlifecycle.action?shipmentNumber=" + shipment_Num+ "&database=" + db;
                    return true;
                }
            }
            }