/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function check()
{
    var value1=document.getElementById("corrattribute1").value;
           
    if (value1!="-1")
        document.getElementById("corr").style.display = "block";
    else
        document.getElementById("corr").style.display = "none";
    var value2=document.getElementById("corrattribute2").value;
    if (value2!="-1")
        document.getElementById("corr1").style.display = "block";
    else
        document.getElementById("corr1").style.display = "none";
          
}                