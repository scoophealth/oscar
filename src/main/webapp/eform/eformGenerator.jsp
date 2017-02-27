<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<%@ page import="oscar.eform.actions.DisplayImageAction,java.lang.*,java.io.File,oscar.OscarProperties,java.io.*,oscar.eform.*,oscar.eform.data.*,java.util.*,org.apache.log4j.Logger"%>
<!--
/*  eForm Generator v5.5e reimagined by Peter Hutten-Czapski 2017
 *
 *  eformGenerator.jsp
 *
 *
 */
-->

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
<title> <bean:message key="eFormGenerator.title"/></title>

<style type="text/css" media="print">
<!-- CSS Script that removes the whole division when printing -->
.DoNotPrint {
	display: none;
}
<!-- CSS Script that removes textarea and textbox borders when printing -->
.noborder {
	border : 0px;
	background: transparent;
	scrollbar-3dlight-color: transparent;
	scrollbar-3dlight-color: transparent;
	scrollbar-arrow-color: transparent;
	scrollbar-base-color: transparent;
	scrollbar-darkshadow-color: transparent;
	scrollbar-face-color: transparent;
	scrollbar-highlight-color: transparent;
	scrollbar-shadow-color: transparent;
	scrollbar-track-color: transparent;
	background: transparent;
	overflow: hidden;
}
</style>
<style>
span.h1		{font-family: sans-serif; font-size: 14px; font-weight: bolder;}
span.h2		{font-family: sans-serif; font-size: 12px; font-weight: bold; text-decoration: underline;}
span.h3		{font-family: sans-serif; font-size: 12px; font-weight: bold;}
span.h4		{font-family: sans-serif; font-size: 12px; font-weight: normal; text-decoration: underline;}
p, li, span	{font-family: sans-serif; font-size: 12px; font-weight: normal;}
a			{font-family: sans-serif; font-size: 12px; font-weight: normal; color: blue; cursor:pointer;}
</style>
<script type='text/javascript'>
/**
 * Enable nudging with arrow keys
 * - Binds to arrow keys to nudge and resize fields
 * - Shift as a modifier switches from move to resize mode
 * - Alt as a modifier changes the step size from 1px to 10px
 *
 *
 * Written By Charlie Livingston <charlie@litsolutions.ca>
*/
document.onkeydown = function(evt) {
    evt = (evt) ? evt : ((window.event) ? event : null);
	var myReturn;
    if (evt) {
		if (evt.altKey) {
		  step = 10;
		} else {
		  step = 1;
		}
        switch (evt.keyCode) {
            case 37:
			    if (evt.shiftKey) {
					changeInput('width', -step);
				} else {
					changeInput('left', step);
				}
				myReturn = false;
                break;
            case 38:
			    if (evt.shiftKey) {
					changeInput('height', -step);
				} else {
					changeInput('up', step);
				}
				myReturn = false;
                break;
            case 39:
			    if (evt.shiftKey) {
					changeInput('width', step);
				} else {
					changeInput('right', step);
				}
				myReturn = false;
                break;
            case 40:
			    if (evt.shiftKey) {
					changeInput('height', step);
				} else {
					changeInput('down', step);
				}
				myReturn = false;
                break;
			default:
				myReturn = true;
				break;
         }
    }
	return myReturn;
}
</script>
<script type="text/javascript">

var BGWidth = 0;
var BGHeight = 0;
var PageNum = 0;
var PageIterate = 1;
var sigint = 0;
var pageoffset = 0;

var SignatureHolderX = 0;
var SignatureHolderY = 0;
var sigOffset = 0; 
var SignatureHolderH = 0;
var SignatureHolderW = 0;
var SignatureHolderP = 0;
var SignatureColor="Black";
var SignatureLineColor="#FFFFFF";
var SignatureBorder="2px dotted blue";
var parentcounter = 0;

function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return String(radioObj.value);
		else
			return String(value);
	for(var k = 0; k < radioLength; k++) {
		if(radioObj[k].checked) {
			return String(radioObj[k].value);
		}
	}
	return "";
}

function loadImage(){
	var img = document.getElementById('imageName');
	var bg = document.getElementById('BGImage');
	if ( img.value == "" ) {
		return
	} 
	if ( bg.src.indexOf(img.value)>0) {
		var r=confirm('<bean:message key="eFormGenerator.loadFileAgain"/>'+img.value+'<bean:message key="eFormGenerator.Again"/>');
		if (r!=true)  {
		  	return
		}
	} 
	//Boilerplate mod to set the path for image function
	bg.src = ("<%=request.getContextPath()%>"+"/eform/displayImage.do?imagefile="+img.value);
	PageNum = PageNum +1;

	DrawPage(jg,PageNum,img.value,bg.width)
	document.getElementById('page').value=PageNum;
	document.getElementById('AutoNamePrefix').value="page"+PageNum+"_";

	loadInputList();	
}

function finishLoadingImage() {

	var img = document.getElementById('imageName');
	var myCnv = document.getElementById('myCanvas');
	var bg = document.getElementById('BGImage');
	
	document.getElementById('OrientCustom').value = document.getElementById('OrientCustomValue').value;
	BGWidth = parseInt(getCheckedValue(document.getElementsByName('Orientation')));
	bg.width = BGWidth;
	if (PageNum >0){
		pageoffset += bg.height;  //the current one
	}
	//alert("Page"+PageNum+"\nPageoffset"+pageoffset+"\nBGHeight"+BGHeight+"\nbg.height"+bg.height+"\nPageNum"+PageNum);
	BGHeight = bg.height;

	document.getElementById('Wizard').style.left = BGWidth;

	myCnv.style.top = bg.style.top;
	myCnv.style.left = bg.style.left;
	myCnv.width = bg.width;
	myCnv.height = bg.height;

	jg.clear();
	drawPageOutline();

}

function drawPageOutline(){
	if (BGWidth <= 800){
		drawPortraitOutline();
	}else if (BGWidth >800) {
		drawLandscapeOutline();
	}
}

function show(x){
	//expands all if x=all
	if (x == 'all'){
		show('Section1');show('Section2');show('Section3');show('Section4');show('Section5');show('Section6');show('Section7');show('Section8');
	}else{
	//expands section
	document.getElementById(x).style.display = 'block';
	}
}
function hide(x){
	//collapse all if x=all
	if (x == 'all'){
		hide('Section1');hide('Section2');hide('Section3');hide('Section4');hide('Section5');hide('Section6');hide('Section7');hide('Section8');
	}else {
	//collapses section
	document.getElementById(x).style.display = 'none';
	}
}

function toggleView(checked,x){
	if (checked){
		document.getElementById(x).style.display = 'block'
	} else if (!checked){
		document.getElementById(x).style.display = 'none';
	}
}

function loadInputList(){
	//load checklist of all eform input fields

	var InputList = document.getElementById('InputList');
	//empty InputList
	while (InputList.childNodes.length>0){
		InputList.removeChild(InputList.lastChild);
	}
	//assign input name into variable 'InputName'
	TempData = DrawData;
	for (var j=0; (j < (TempData.length) ); j++){
		var RedrawParameter = TempData[j].split("|");
		var InputType = RedrawParameter[0]
		var InputName = "";

		if (InputType == 'Text'){
			InputName = new String(RedrawParameter[5]);
		}else if (InputType == 'Textbox'){
			InputName= new String(RedrawParameter[5]);
		}else if (InputType == 'Checkbox'){
			InputName = new String(RedrawParameter[3]);
		}else if (InputType == 'Xbox'){
			InputName = new String(RedrawParameter[5]);
		}else if (InputType == 'Page'){
			InputName = "--Page--";
		}else if (InputType == 'Signature'){
			InputName = new String(RedrawParameter[5]);
		}else if (InputType == 'Stamp'){
			InputName = new String(RedrawParameter[5]);
		}
		//adds InputName as list item in InputList
		var ListItem = document.createElement("li");
		var txt = "<input name='InputChecklist' type='checkbox' id='" + InputName + "' value ='" + InputName+ "'>" + InputName;
		ListItem.innerHTML = txt;
		InputList.appendChild(ListItem);
	}
	//if (document.getElementById('AddSignatureClassic').checked){
	//	ListItem = document.createElement("li");
	//	ListItem.innerHTML = "<input name='InputChecklist' type='checkbox' id='SignatureBox' value ='SignatureBox'>SignatureBox";
	//	InputList.appendChild(ListItem);
	//}
}

function addToUserSignatureList(){
	var UserSignatureList = document.getElementById('UserSignatureList');	//adds User Name and Signature Image Filename to UserSignature List, separated by '|'
	var UserName = document.getElementById('UserList').value;
	var FileName = document.getElementById('SignatureList').value;

	var ListItem = document.createElement("li");
	ListItem.setAttribute('name', 'UserSignatureListItem');
	var UserSignature = UserName + '|' + FileName;
	ListItem.innerHTML = UserSignature;
	UserSignatureList.appendChild(ListItem);
}

function emptyUserSignaturelist(){
	var UserSignatureList = document.getElementById('UserSignatureList');	//Empty UserSignature List
	//empty UserSignatureList
	while (UserSignatureList.childNodes.length>0){
		UserSignatureList.removeChild(UserSignatureList.lastChild);
	}
}



function uncheckList(x){
	var List = document.getElementsByName(x);
	for (i=0; i < List.length; i++){
		List[i].checked = false;
	}
}
function checkList(x){
	var List = document.getElementsByName(x);
	for (i=0; i < List.length; i++){
		List[i].checked = true;
	}
}

function changeInput(d,p){
var InputChecklist = document.getElementsByName('InputChecklist');
	for (i=0; i < InputChecklist.length; i++){
		if (InputChecklist[i].checked){
			var n = InputChecklist[i].value;
			TransformInput(n,d,p);
		}
	}
}


function TransformInput(n, d, p){
//parses DrawData and find InputName = n,
//then shift the inputbox p pixels in direction d (up, down, left, right)
// if d = 'width' or 'height', the width and height will change by p pixels

	TempData = DrawData;
	var InputName = ""	//hold InputName
	var	DataNumber	= parseInt(0)	//holds the number that correspond to the order in which the Input is entered into the array
	p = parseInt(p);

	//shift Text, Textbox, Checkboxes
	for (var j=0; (j < (TempData.length)); j++){
		var RedrawParameter = TempData[j].split("|");
		var InputType = RedrawParameter[0]
		if (InputType == 'Text'){
			InputName = new String(RedrawParameter[5]);
			DataNumber = j;
		}else if (InputType == 'Textbox'){
			InputName = new String(RedrawParameter[5]);
			DataNumber = j;			
		}else if (InputType == 'Checkbox'){
			InputName = new String(RedrawParameter[3]);
			DataNumber = j;
		}else if (InputType == 'Xbox'){
			InputName = new String(RedrawParameter[5]);
			DataNumber = j;
		}else if (InputType == 'Signature'){
			InputName = new String(RedrawParameter[5]);
			DataNumber = j;
		}else if (InputType == 'Stamp'){
			InputName = new String(RedrawParameter[5]);
			DataNumber = j;
		}
		if (InputName == n){		//if InputName matches n
			var TargetParameter = TempData[DataNumber].split("|");
			var Xcoord = parseInt(TargetParameter[1]);
			var Ycoord = parseInt(TargetParameter[2]);
			var W = parseInt(TargetParameter[3]);
			var H = parseInt(TargetParameter[4]);
			if (d == 'up'){
				Ycoord = Ycoord - p;
				TargetParameter[2] = Ycoord;
			} else if (d == 'down'){
				Ycoord = Ycoord + p;
				TargetParameter[2] = Ycoord;
			} else if (d == 'left'){
				Xcoord = Xcoord - p;
				TargetParameter[1] = Xcoord;
			} else if (d == 'right'){
				Xcoord = Xcoord + p;
				TargetParameter[1] = Xcoord;
			} else if (d == 'width'){
				W = W + p;
				TargetParameter[3] = W;
			} else if (d == 'height'){
				H = H + p;
				TargetParameter[4] = H;
			}
		DrawData[j] = TargetParameter.join("|");
		}
	}

	//Redraw boxes after updating coordinates
	RedrawAll();
}


var TopEdge = parseInt(0);
var BottomEdge = parseInt(0);
var LeftEdge = parseInt(0);
var RightEdge = parseInt(0);


function alignInput(edge){
//finds checked InputName, then aligns checked input boxes to top/bottom/left/right edge
	TempData = DrawData;
	var InputChecklist = document.getElementsByName('InputChecklist');
	var InputName = "";		//hold InputName
	var DataNumber	= parseInt(0);	//holds the number that correspond to the order in which the Input is entered into the array
	var Initialized = false;

	//find the inputs with the most top/bottom/left/right coordinates
	for (i=0; i < InputChecklist.length; i++){

		if (InputChecklist[i].checked){
			var n = InputChecklist[i].value;	//finds name of checked input, assigns it to n

			for (var j=0; (j < (TempData.length)); j++){
				var RedrawParameter = TempData[j].split("|");
				var InputType = RedrawParameter[0]
				
				if (InputType == 'Text'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Textbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;			
				}else if (InputType == 'Checkbox'){
					InputName = new String(RedrawParameter[3]);
					DataNumber = j;
				}else if (InputType == 'Xbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Signature'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Stamp'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}
				if (InputName == n){
					var TargetParameter = TempData[DataNumber].split("|");
					var Xcoord = parseInt(TargetParameter[1]);
					var Ycoord = parseInt(TargetParameter[2]);
					if (!Initialized){
						TopEdge = Ycoord;
						BottomEdge = Ycoord;
						LeftEdge = Xcoord;
						RightEdge = Xcoord;
						Initialized = true;
					}
					
					if (Xcoord < LeftEdge){
						LeftEdge = Xcoord;
					}else if (Xcoord > RightEdge){
						RightEdge = Xcoord;
					} 
					if (Ycoord < TopEdge){
						TopEdge = Ycoord;
					}else if (Ycoord > BottomEdge){
						BottomEdge = Ycoord;
					}			
				}
			}
		}
	}
	
	//change selected inputs' coordinates to top/bottom/left/right edges
	for (i=0; i < InputChecklist.length; i++){
		if (InputChecklist[i].checked){
			var n = InputChecklist[i].value;	//finds name of checked input, assigns it to n
			for (var j=0; (j < (TempData.length)); j++){
				var RedrawParameter = TempData[j].split("|");
				var InputType = RedrawParameter[0]
				if (InputType == 'Text'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;				
				}else if (InputType == 'Textbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;			
				}else if (InputType == 'Checkbox'){
					InputName = new String(RedrawParameter[3]);
					DataNumber = j;
				}else if (InputType == 'Xbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Signature'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Stamp'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}
				if (InputName == n){		//if InputName matches n
					var TargetParameter = TempData[DataNumber].split("|");
					var Xcoord = parseInt(TargetParameter[1]);
					var Ycoord = parseInt(TargetParameter[2]);
					if (edge == 'top'){
						TargetParameter[2] = TopEdge;
					}else if (edge == 'bottom'){
						TargetParameter[2] = BottomEdge;
					}else if (edge == 'left'){
						TargetParameter[1] = LeftEdge;
					}else if (edge == 'right'){
						TargetParameter[1] = RightEdge;
					}	
					DrawData[DataNumber] = TargetParameter.join("|");
				}
			}

		}
	
	}
	
	//Redraw boxes after updating coordinates
	RedrawAll();
}


function deleteInput(){
	TempData = DrawData;
	var InputChecklist = document.getElementsByName('InputChecklist');
	var InputName = ""	//hold InputName
	var	DataNumber	= parseInt(0)	//holds the number that correspond to the order in which the Input is entered into the array

	//delete checked inputs in the input checklist
	for (i=0; i < InputChecklist.length; i++){

		if (InputChecklist[i].checked){
			var n = InputChecklist[i].value;	//finds name of checked input, assigns it to n

			for (var j=0; (j < (TempData.length)); j++){
				var RedrawParameter = TempData[j].split("|");
				var InputType = RedrawParameter[0]

				if (InputType == 'Text'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Textbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;			
				}else if (InputType == 'Checkbox'){
					InputName = new String(RedrawParameter[3]);
					DataNumber = j;
				}else if (InputType == 'Xbox'){
					InputName = new String(RedrawParameter[5]);
					DataNumber = j;
				}else if (InputType == 'Signature'){
					InputName = new String(RedrawParameter[5]);
					//document.getElementById('AddSignature').checked=false;
					//document.getElementById('AddSignature').disabled=false;
					//document.getElementById('AddSignatureBox1').disabled=false;
					DataNumber = j;
				}else if (InputType == 'Stamp'){
					InputName = new String(RedrawParameter[5]);
					document.getElementById('AddStamp').checked=false;
					document.getElementById('AddStamp').disabled=false;
					document.getElementById('AddSignatureBox2').disabled=false;
					DataNumber = j;
				}
				if (InputName == n){
					TempData.splice(j,1);
				}
			}
		}
	}
	DrawData = TempData;
	//Redraw boxes after updating coordinates
	RedrawAll();
	loadInputList();
}
</script>

<script type="text/javascript">
//output html code for eform

var text = "";
var textTop = "";
var textMiddle = "";
var textBottom = "";

var CheckboxOffset = 4;
var XboxOffset = 4;

var MTopLeftX = 0;
var MTopLeftY = 0;
var FTopLeftX = 0;
var FTopLeftY = 0;
var SignatureHolderX = 0;
var SignatureHolderY = 0;
var SignatureHolderH = 0;
var SignatureHolderW = 0;

function resetAll(){
	text = "";
	textTop = "";
	textBottom = "";
	textMiddle = "";
	inputName = "";
	inputCounter = 1;
	DrawData = new Array();
	TempData = new Array();

	SetSwitchOn('Text');
	document.getElementById('Text').click();

	document.getElementById('inputValue').value = "";
	document.getElementById('inputName').value = "";
	document.getElementById('page').value="";
	document.getElementById('preCheck').checked = false;
	document.getElementById('preCheckGender').checked = false;
	document.getElementById('XboxType').checked = false;
	document.getElementById('maximizeWindow').checked = false;
	var l = document.getElementById('oscarDB');
		l[0].selected = true;
	document.getElementById('AddSignature').checked = false;
	document.getElementById('AddStamp').checked = false;
	document.getElementById('AddSignatureClassic').checked = false;
	document.getElementById('AddSignature').disabled=false;
	document.getElementById('AddSignatureClassic').disabled=false;
	document.getElementById('AddSignatureBox1').disabled=false;
	document.getElementById('AddStamp').disabled=false; 
	document.getElementById('AddSignatureBox2').disabled=false;
	//document.getElementById('DefaultCheckmark').checked = false;

	<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_print_enabled")) { %>
		document.getElementById('includePdfPrintControl').checked = false;
	<%}%>
	
	<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_fax_enabled")) { %>
		document.getElementById('includeFaxControl').checked = false;
	<% } %>
	document.getElementById('BlackBox').checked = false;
	clearGraphics(jg);
	PageNum=0;
	finishLoadingImage();
	loadImage();
}

function GetTextTop(){
	textTop = "&lt;html&gt;\n&lt;head&gt;\n"
	textTop += "&lt;META http-equiv=&quot;Content-Type&quot; content=&quot;text/html; charset=UTF-8&quot;&gt;\n"
	textTop += "&lt;title&gt;"
	textTop += document.getElementById('eFormName').value;
	textTop += "&lt;/title&gt;\n"
	textTop += "&lt;style type=&quot;text/css&quot; media=&quot;screen&quot; &gt;\n";
	textTop += "input {\n\t-moz-box-sizing: content-box;\n\t-webkit-print-color-adjust: exact;\n\t-webkit-box-sizing: content-box;\n\tbox-sizing: content-box\n}\n .sig {\n\tborder: "+SignatureBorder+";\n\tcolor: "+SignatureColor+";\n\tbackground-color: white;\n }\n    /* Drawing the 'gripper' for touch-enabled devices */\n    html.touch #content {\n        float:left;\n        width:92%;\n    }\n    html.touch #scrollgrabber {\n        float:right;\n        width:4%;\n        margin-right:2%;\n        background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAFCAAAAACh79lDAAAAAXNSR0IArs4c6QAAABJJREFUCB1jmMmQxjCT4T/DfwAPLgOXlrt3IwAAAABJRU5ErkJggg==)\n    }\n    html.borderradius #scrollgrabber {\n        border-radius: 1em;\n    }\n"
	textTop += " &lt;/style&gt;\n\n";

	textTop += "&lt;style type=&quot;text/css&quot; media=&quot;print&quot;&gt;\n"
	textTop += "\n .DoNotPrint {\n\tdisplay: none;\n }\n .noborder {\n\tborder : 0px;\n\tbackground: transparent;\n\tscrollbar-3dlight-color: transparent;\n\tscrollbar-3dlight-color: transparent;\n\tscrollbar-arrow-color: transparent;\n\tscrollbar-base-color: transparent;\n\tscrollbar-darkshadow-color: transparent;\n\tscrollbar-face-color: transparent;\n\tscrollbar-highlight-color: transparent;\n\tscrollbar-shadow-color: transparent;\n\tscrollbar-track-color: transparent;\n\tbackground: transparent;\n\toverflow: hidden;\n }\n.sig {\n\tborder-style: solid;\n\tborder-color: transparent;\n\tcolor: "+SignatureColor+";\n\tbackground-color: transparent;\n }\n\n "
	textTop += "&lt;/style&gt;\n\n";

	textTop += "&lt;!-- jQuery for greater functionality --&gt;\n"
	// dependency on jquery up to version 2.2.1 for pdf and faxing hack. (3.1.1 does NOT work.)  Lets reference something off the OSCAR server
	textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js&quot;&gt;&lt;/script&gt;\n";	
	// if unavailable reference the one in OSCAR
	textTop += "&lt;script&gt; window.jQuery || document.write('&lt;script src=&quot;../js/jquery-1.7.1.min.js&quot;&gt;&lt; &#92;/script&gt;') &lt;/script&gt;\n";

	// ole darn it, I knew I left a copy of jQuery lying around somewhere... perhaps under my nose?
	textTop += "&lt;script&gt; window.jQuery || document.write('&lt;script src=&quot;jquery-1.7.1.min.js&quot;&gt;&lt; &#92;/script&gt;') &lt;/script&gt;\n\n";
	
	// Adding jquery code for parent-child-fields (Bell Eapen, nuchange.ca)
	textTop += "<!-- jQuery for parent-child-fields -->\n"
    textTop += "&lt;script&gt;\n";
	textTop += "$(document).ready(function() {\n            $('[class^=\"child-\"]').hide();\n            $('.parent-field').click(function() {\n                $('[class^=\"child-\"]').hide();\n                $('.parent-field').each(function() {\n                    if ( $(this).is('input:checkbox') ){\n                         if(this.checked){\n                            $('.child-' +  $(this).prop('id')).show();\n                        }\n                    }else{\n                        $('.child-' + $(this).val()).show();\n                    }\n                });\n            }).trigger('change');\n\n            $('[class^=\"only-one-\"]').click(function() {\n                $('.'+$(this).attr('class')).prop('checked', false);\n                $(this).prop('checked', true);\n                $(this).closest('div').find('.parent-field').change();\n            });\n});";
    textTop += "&lt;/script&gt;\n"
	textTop += "<!-- jQuery for parent-child-fields -->\n"
    textTop += "&lt;/script&gt;\n"

	//reference built in functions as desired

<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_print_enabled")) { %>
	if (document.getElementById('includePdfPrintControl').checked) {
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;$%7Boscar_javascript_path%7Deforms/printControl.js&quot;&gt;&lt;/script&gt;\n";
	}
	//fax number script
	if ((document.getElementById('faxno').value.length > 0)){
		textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
		textTop += "function setFaxNo(){\n"
		textTop += "\tsetTimeout('document.getElementById(&quot;otherFaxInput&quot;).value=&quot;"
		textTop += document.getElementById('faxno').value
		textTop += "&quot;',1000);\n"
		textTop += "} \n"
		textTop += "&lt;/script&gt;\n\n"	
	}
<% }%>

	//reference built in faxControl	
<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_fax_enabled")) { %>
		if (document.getElementById("includeFaxControl").checked) {
			textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;$%7Boscar_javascript_path%7Deforms/faxControl.js&quot;&gt;&lt;/script&gt;\n";
	}
<% } %>


	//reference built in signatureControl
	if (document.getElementById('AddSignatureClassic').checked){
			
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;$%7Boscar_javascript_path%7Deforms/signatureControl.jsp&quot;&gt;&lt;/script&gt;\n";
		textTop += "&lt;script type=&quot;text/javascript&quot;&gt;\n";	
		textTop += "if (typeof jQuery != &quot;undefined&quot; &amp;&amp; typeof signatureControl != &quot;undefined&quot;) {";
		textTop += "jQuery(document).ready(function() {";
		var totalpx = SignatureHolderY + sigOffset;
		textTop += "signatureControl.initialize({eform:true, height:"+SignatureHolderH+", width:"+SignatureHolderW+", top:"+totalpx+", left:"+SignatureHolderX+"});";
		textTop += "});}";
		textTop +="&lt;/script&gt;\n";
		
	}


	//reference Signature library
	if (document.getElementById('AddSignature').checked){

		var sigArray = new Array();
		for (j=0; (j < (DrawData.length) ); j++){
			var P = DrawData[j].split("|");
			if ((P[0]=="Signature")&& (P[5] != "ClassicSignature")) {
				sigArray.push(P[5]);		
			}
		}

		textTop += "\n&lt;!-- Freeform Signatures --&gt;\n"
		//In OSCAR 15 jSignature is available within the source  
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;${oscar_javascript_path}/jquery/jSignature.min.js&quot;&gt;&lt;/script&gt;\n\n";
		textTop += "&lt;!--[if lt IE 9]&gt;\n"
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;${oscar_javascript_path}flashcanvas.js&quot;&gt;&lt;/script&gt;\n";
		textTop += "&lt;![endif]--&gt;\n\n"


		textTop += "&lt;script type=&quot;text/javascript&quot;&gt;\n";	
		textTop += "jQuery(document).ready(function() {\n";
		for (j=0; (j < (sigArray.length) ); j++){
			textTop += "\t$(&quot;#Canvas"+sigArray[j]+"&quot;).jSignature({'decor-color':'"+SignatureLineColor+"'})\n"
		}
		textTop += "\tvar pdf = jQuery(&quot;input[name='pdfButton']&quot;);\n"
		textTop += "\tif (pdf.size() != 0) {\n"
		textTop += "\t\tpdf.attr('onclick', '').unbind('click');\n"
		textTop += "\t\tpdf.attr('value', 'PDF');\n"
		textTop += "\t\tpdf.click(function(){saveSig();submitPrintButton(false);});\n"
		textTop += "\t\t}\n"
		textTop += "\tvar pdfSave = jQuery(&quot;input[name='pdfSaveButton']&quot;);\n"
		textTop += "\tif (pdfSave.size() != 0) {\n"
		textTop += "\t\tpdfSave.attr('onclick', '').unbind('click');\n"
		textTop += "\t\tpdfSave.attr('value', 'Submit & PDF');\n"
		textTop += "\t\tpdfSave.click(function(){saveSig();submitPrintButton(true);});\n"
		textTop += "\t\t}\n"

		textTop += "})\n";
		textTop +="&lt;/script&gt;\n\n";
		textTop += "&lt;script type=&quot;text/javascript&quot;&gt;\n";
		textTop += "function saveSig(){\n"
		for (j=0; (j < (sigArray.length) ); j++){
			textTop += "\tvar $sig=$(&quot;#Canvas"+sigArray[j]+"&quot;);\n"
			textTop += "\tvar datapair=new Array();\n"
			textTop += "\tdatapair=$sig.jSignature(&quot;getData&quot;,&quot;base30&quot;);\n"
			textTop += "\tdocument.getElementById(&quot;Store"+sigArray[j]+"&quot;).value=datapair.join(&quot;,&quot;);\n"
		}
		textTop += "}\n";
		textTop += "function clearSig(){\n"
		for (j=0; (j < (sigArray.length) ); j++){
			textTop += "\t$(&quot;#Canvas"+sigArray[j]+"&quot;).jSignature(&quot;reset&quot;);\n"
		}
		textTop += "}\n";

		textTop += "function loadSig(){\n"
		for (j=0; (j < (sigArray.length) ); j++){
			textTop += "\tvar $sig=$(&quot;#Canvas"+sigArray[j]+"&quot;);\n"
			textTop += "\tvar data\n"
			textTop += "\tdata=document.getElementById(&quot;Store"+sigArray[j]+"&quot;).value;\n"
			textTop += "\t$sig.jSignature(&quot;setData&quot;,&quot;data:&quot;+ data) ;\n"
		}
		textTop += "}\n";
		textTop += "&lt;/script&gt;\n\n"
	}


	//auto ticking gender Xboxes OR checkboxes
	if ((document.getElementById('preCheckGender').checked)||(document.getElementById('XboxType').checked)){
		textTop += "&lt;!-- auto ticking gender Xboxes OR checkboxes --&gt;\n"	
		textTop += "&lt;script type=&quot;text/javascript&quot; language=&quot;javascript&quot;&gt;\n"
		textTop += "function checkGender(){\n"
		textTop += "\t if (document.getElementById(&quot;PatientGender&quot;).value == &quot;M&quot;){\n"
		textTop += "\t document.getElementById(&quot;Male&quot;).click();\n"
		textTop += "\t }else if (document.getElementById(&quot;PatientGender&quot;).value == &quot;F&quot;){\n"
		textTop += "\t document.getElementById(&quot;Female&quot;).click();\n"
		textTop += "\t}\n }\n"
		textTop += "&lt;/script&gt;\n\n"
	}

	//printing script
	textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
	textTop += "function formPrint(){\n"
	textTop += "\twindow.print();\n"
	textTop += "} \n"
	textTop += "&lt;/script&gt;\n\n"

	//Peter Hutten-Czapski's script to confirm closing of window if eform changed
	textTop += "&lt;!-- scripts to confirm closing of window if it hadnt been saved yet --&gt;\n"
	textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
	textTop += "//keypress events trigger dirty flag\n"
	textTop += "var needToConfirm = false;\n"
	textTop += "document.onkeyup=setDirtyFlag;\n"
	textTop += "function setDirtyFlag(){\n"
	textTop += "\tneedToConfirm = true;\n"
	textTop += "}\n"
	textTop += "function releaseDirtyFlag(){\n"
	textTop += "\tneedToConfirm = false; //Call this function to prevent an alert.\n"
	textTop += "//this could be called when save button is clicked\n"
	textTop += "}\n"
	textTop += "window.onbeforeunload = confirmExit;\n"
	textTop += "function confirmExit(){\n"
	textTop += "\tif (needToConfirm){\n"
	textTop += "\t\t return &quot;You have attempted to leave this page. If you have made any changes to the fields without clicking the Save button, your changes will be lost. Are you sure you want to exit this page?&quot;;\n"
	textTop += "\t }\n"
	textTop += "}\n"
	textTop += "&lt;/script&gt;\n\n"

	//Peter Hutten-Czapski's Xbox scripts

	var xPresent=false;
	for (j=0; (j < (DrawData.length) ); j++){
		var P = DrawData[j].split("|");
		if (P[0]=="Xbox") {
			xPresent=true;		
		}
	}
	
	if (xPresent){
		textTop += "&lt;!-- scripts for Xbox functions --&gt;\n"
		textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
		textTop += "$(document).ready(function() {\n"
		textTop += "\t  $( &quot;.Xbox&quot; ).click(function() {\n"
		if (document.getElementById('BlackBox').checked){
			textTop += "\t\t  var bc = $( this ).css( &quot;background-color&quot; );\n"
			textTop += "\t\t  if (bc==&quot;rgb(0, 0, 0)&quot;) {\n"
			textTop += "\t\t\t  $( this ).css( &quot;background-color&quot;, &quot;white&quot; );\n"
			textTop += "\t\t\t  $( this ).val(&quot;&quot;);\n"
			textTop += "\t\t  } else {\n"
			textTop += "\t\t\t  $( this ).css( &quot;background-color&quot;, &quot;rgb(0, 0, 0)&quot; );\n"
		} else {
			textTop += "\t\t  var st = $( this ).val();\n"
			textTop += "\t\t  if (st==&quot;X&quot;) {\n"
			textTop += "\t\t\t  $( this ).val(&quot;&quot;);\n"
			textTop += "\t\t  } else {\n"
		}
		textTop += "\t\t\t  $( this ).val(&quot;X&quot;);\n"
		textTop += "\t\t  }\n"
		textTop += "\t});\n\n"

		textTop += "$( &quot;.Xbox&quot; ).keypress(function(event) {\n"
		textTop += "\t  // any key press except tab will constitute a value change to the checkbox\n"
		textTop += "\t  if (event.which != 0){\n"
		textTop += "\t\t  $( this ).click();\n"
		textTop += "\t\t  return false;\n"
		textTop += "\t\t  }\n"
		textTop += "\t});\n\n"
		textTop += "});\n"
		textTop += "&lt;/script&gt;\n\n"
	}

	//maximize window script
	if (document.getElementById('maximizeWindow').checked){
		textTop += "&lt;script language=&quot;JavaScript&quot;&gt;\n"
		textTop += "\t top.window.moveTo(0,0);\n"
		textTop += "\t if (document.all) {\n"
		textTop += "\t top.window.resizeTo(screen.availWidth,screen.availHeight);\n"
		textTop += "\t }\n"
		textTop += "\t else if (document.layers||document.getElementById) {\n"
		textTop += "\t if (top.window.outerHeight&lt;screen.availHeight||top.window.outerWidth&lt;screen.availWidth){\n"
		textTop += "\t\t top.window.outerHeight = screen.availHeight;\n"
		textTop += "\t\t top.window.outerWidth = screen.availWidth;\n"
		textTop += "\t}\n}\n &lt;/script&gt;\n\n"
	}
	//scripts for scaling up checkboxes
	if (document.getElementById('ScaleCheckmark').checked){
		textTop += "&lt;!-- scripts for scaling up checkboxes --&gt;\n"	
		textTop += "&lt;style type=&quot;Text/css&quot;&gt;\n"
		textTop += "input.largerCheckbox {\n"
		textTop += "\t-moz-transform:scale(1.3);         /*scale up image 1.3x - Firefox specific */ \n"
		textTop += "\t-webkit-transform:scale(1.3);      /*Webkit based browser eg Chrome, Safari */ \n"
		textTop += "\t-o-transform:scale(1.3);           /*Opera browser */ \n"
		textTop += "}\n"
		textTop += "&lt;/style&gt;\n"
		textTop += "&lt;style type=&quot;text/css&quot; media=&quot;print&quot;&gt;\n"
		textTop += "input.largerCheckbox { \n"
		textTop += "\t-moz-transform:scale(1.8);         /*scale up image 1.8x - Firefox specific */ \n"
		textTop += "\t-webkit-transform:scale(1.3);      /*Webkit based browser eg Chrome, Safari */ \n"
		textTop += "\t-o-transform:scale(1.3);           /*Opera browser */ \n"
		textTop += "} \n"
		textTop += "&lt;/style&gt;\n"
		textTop += "&lt;!--[if IE]&gt;\n"
		textTop += "&lt;style type=&quot;text/css&quot;&gt;\n"
		textTop += "input.largerCheckbox { \n"
		textTop += "\theight: 30px;                     /*30px checkboxes for IE 5 to IE 7 */ \n"
		textTop += "\twidth: 30px; \n"
		textTop += "} \n"
		textTop += "&lt;/style&gt; \n"
		textTop += "&lt;![endif]--&gt; \n\n"
	}

	if (document.getElementById('AddStamp').checked){

		var List = document.getElementsByName('UserSignatureListItem');

		textTop += "&lt;!-- Stamped Signatures --&gt;\n"
		textTop += "&lt;script type=&quot;text/javascript&quot;&gt;\n"
		textTop += "//autoloading signature images\n"
		textTop += "var ImgArray = [\n\t&quot;anonymous|BNK.png&quot;"
		for (i=0; i<List.length;i++){
			textTop +=",\n\t&quot;"+List[i].innerHTML.trim()+"&quot;"
		}
		textTop += "\n\t];\n\n"
		textTop += "function SignForm(){\n"
		textTop += "\t//first look for the current users stamp\n"
		textTop += "\tfor (i=0; i&lt;ImgArray.length;i++){\n"
		textTop += "\t\tvar ListItemArr =  ImgArray[i].split(&quot;|&quot;);\n"
		textTop += "\t\tvar UserName = ListItemArr[0];\n"
		textTop += "\t\tvar FileName = ListItemArr[1];\n"
		textTop += "\t\tif (document.getElementById(&quot;CurrentUserName&quot;).value.indexOf(UserName)>=0){\n"
		textTop += "\t\t\tdocument.getElementById(&quot;Stamp&quot;).src = &quot;../eform/displayImage.do?imagefile=&quot;+FileName;\n"
		textTop += "\t\t}\n"
		textTop += "\t}\n"
		textTop += "\t//hmm not found so lets try the MRPs stamp\n"
		textTop += "\tif (document.getElementById(&quot;Stamp&quot;).src.indexOf(&quot;BNK.png&quot;)>0){\n"
		textTop += "\t\tfor (i=0; i&lt;ImgArray.length;i++){\n"
		textTop += "\t\t\tvar ListItemArr =  ImgArray[i].split(&quot;|&quot;);\n"
		textTop += "\t\t\tvar UserName = ListItemArr[0];\n"
		textTop += "\t\t\tvar FileName = ListItemArr[1];\n"
		textTop += "\t\t\tif (document.getElementById(&quot;DoctorName&quot;).value.indexOf(UserName)>=0){\n"
		textTop += "\t\t\t\tdocument.getElementById(&quot;Stamp&quot;).src = &quot;../eform/displayImage.do?imagefile=&quot;+FileName;\n"
		textTop += "\t\t\t}\n"
		textTop += "\t\t}\n"
		textTop += "\t}\n"
		textTop += "}\n"

		textTop += "function toggleMe(){\n"
		textTop += "\tif (document.getElementById(&quot;Stamp&quot;).src.indexOf(&quot;BNK.png&quot;)>0){\n"
		textTop += "\t\tSignForm()\n"
		textTop += "\t} else {\n"
		textTop += "\t\tdocument.getElementById(&quot;Stamp&quot;).src = &quot;../eform/displayImage.do?imagefile=BNK.png&quot;;\n"
		textTop += "\t}\n"
		textTop += "}\n"
		textTop += "&lt;/script&gt;\n\n"
	
	}
	if (document.getElementById('AddDate').checked){
		textTop +="\n&lt;!-- main calendar program --&gt;\n"
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;../share/calendar/calendar.js&quot;&gt;&lt;/script&gt;\n"
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;../share/calendar/lang/<bean:message key="global.javascript.calendar"/>&quot;&gt;&lt;/script&gt;\n"
		textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;../share/calendar/calendar-setup.js&quot;&gt;&lt;/script&gt;\n"
		textTop += "&lt;link rel=&quot;stylesheet&quot; type=&quot;text/css&quot; media=&quot;all&quot; href=&quot;../share/calendar/calendar.css&quot; title=&quot;win2k-cold-1&quot; /&gt;\n\n"

	}



	//</head>
	textTop += "&lt;/head&gt;\n\n"
	//<body>
	textTop += "&lt;body"
	textTop += " onload=&quot;"
	//auto check gender boxes
	if ((document.getElementById('preCheckGender').checked)||(document.getElementById('XboxType').checked)){
		textTop += "checkGender();"
	}
	//auto load signature stamp image, default to 'current_user'
	if (document.getElementById('AddStamp').checked){
		textTop += "SignForm();"
	}
	if (document.getElementById('AddSignature').checked){
		textTop += "loadSig();"
	}
	if ((document.getElementById('faxno').value.length >0)){
		textTop += "setFaxNo();"
	}
	textTop += "&quot;&gt;\n"

	//<form>
	textTop +="&lt;form method=&quot;post&quot; action=&quot;&quot; name=&quot;FormName&quot; id=&quot;FormName&quot; &gt;\n";

}

function GetTextMiddle(P){

var InputType = P[0];
    console.log(P);
	if (InputType == "Page"){

		var pg = parseInt(P[1]);
		var im = P[2];
		var width = parseInt(P[3]);
		m = "";
		if (pg > 1){m = "&lt;/div&gt;\n\n\n";}
		m += "&lt;div id=&quot;page"
		m += pg
		m += "&quot; style=&quot;page-break-after:always;position:relative;&quot; &gt;\n\n"
		m += "&lt;img id=&quot;BGImage"
		m += pg
		m += "&quot; src=&quot;$%7Boscar_image_path%7D";
		m += im
		m += "&quot; style=&quot;position: relative; left: 0px; top: 0px; width:"
		m += width
		m += "px&quot;&gt;\n"
		
	}

	if (InputType == "Text"){
		var x0 = parseInt(P[1]);
		var y0 = parseInt(P[2]);
		var width = parseInt(P[3]);
		var height = parseInt(P[4]);
		var inputName = P[5];
		var fontFamily = P[6];
		var fontStyle = P[7];
		var fontWeight = P[8];
		var fontSize = P[9];
		var textAlign = P[10];
		var bgColor = P[11];
		var oscarDB = P[12];
		var inputValue = P[13];
		var inputClassValue = P[14]+P[15];
		m = "&lt;input name=&quot;" 
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; type=&quot;text&quot; class=&quot;"
		m += inputClassValue
		m += " noborder&quot; style=&quot;position:absolute; left:"
		m += x0
		m += "px; top:"
		m += y0
		m += "px; width:"
		m += width
		m += "px; height:"
		m += height
		m += "px; font-family:"
		m += fontFamily
		m += "; font-style:"
		m += fontStyle
		m += "; font-weight:"
		m += fontWeight
		m += "; font-size:"
		m += fontSize
		m += "px; text-align:"
		m += textAlign
		m += "; background-color:"
		m += bgColor
		m += ";&quot; "
		if (oscarDB){
			m += " oscarDB="
			m += oscarDB
		}else if (!oscarDB){
			m += "value=&quot;"
			m += inputValue
			m += "&quot;"
		}
		m += "&gt;\n"

	} else if (InputType == "Textbox"){
		var x0 = parseInt(P[1]);
		var y0 = parseInt(P[2]);
		var width = parseInt(P[3]);
		var height = parseInt(P[4]);
		var inputName = P[5];
		var fontFamily = P[6];
		var fontStyle = P[7];
		var fontWeight = P[8];
		var fontSize = P[9];
		var textAlign = P[10];
		var bgColor = P[11];
		var oscarDB = P[12];
		var inputValue = P[13];
		var inputClassValue = P[14]+P[15];
		m = "&lt;textarea name=&quot;"
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; type=&quot;text&quot; class=&quot;"
		m += inputClassValue
		m += " noborder&quot; style=&quot;position:absolute; left:"
		m += x0
		m += "px; top:"
		m += y0
		m += "px; width:"
		m += width
		m += "px; height:"
		m += height
		m += "px; font-family:"
		m += fontFamily
		m += "; font-style:"
		m += fontStyle
		m += "; font-weight:"
		m += fontWeight
		m += "; font-size:"
		m += fontSize
		m += "px; text-align:"
		m += textAlign
		m += "; background-color:"
		m += bgColor
		m += ";&quot; "
		if (oscarDB){
			m += " oscarDB="
			m += oscarDB
		}
		m += "&gt;"
		if (!oscarDB) {
			m += inputValue
		}
		m += "&lt;/textarea&gt;\n"
	
	} else if (InputType == "Checkbox"){
		var x = parseInt(P[1]);
		var y = parseInt(P[2]);
		var inputName = P[3];
		var preCheck = P[4];
		var inputClassValue = P[5]+P[6];
		m = "&lt;input name=&quot;" 
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; class=&quot;"
        m += inputClassValue
		m += "&quot; type=&quot;checkbox&quot;"
		if (document.getElementById('ScaleCheckmark').checked){
			m += " class=&quot;largerCheckbox&quot;"
		}
		m += " style=&quot;position:absolute; left:"
		var a = parseInt(x - XboxOffset);
		m += a
		m += "px; top:"
		var b = parseInt(y - XboxOffset);
		m += b
		m += "px; &quot;"
		m += "&gt;\n"

	} else if (InputType == "Xbox"){
		var x0 = parseInt(P[1]);
		var y0 = parseInt(P[2]);
		var width = parseInt(P[9])+2;
		var height = width;
		var inputName = P[5];
		var fontFamily = P[6];
		var fontStyle = P[7];
		var fontWeight = P[8];
		var fontSize = P[9];
		var textAlign = P[10];
		var bgColor = P[11];
		var oscarDB = P[12];
		var inputValue = P[13];
		m = "&lt;input name=&quot;" 
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; type=&quot;text&quot; class=&quot;Xbox&quot; style=&quot;position:absolute; left:"
		m += x0
		m += "px; top:"
		m += y0
		m += "px; width:"
		m += width
		m += "px; height:"
		m += height
		m += "px; font-family:"
		m += fontFamily
		m += "; font-style:"
		m += fontStyle
		m += "; font-weight:"
		m += "bold"
		m += "; font-size:"
		m += fontSize
		m += "px; text-align:"
		m += "center"
		m += "; background-color:"
		if ((document.getElementById('BlackBox').checked) && (inputValue=='X')) {
			m +="rgb(0,0,0)"
		} else {
			m += bgColor
		}
		m += ";&quot; "
		m += "value=&quot;"
		m += inputValue
		m += "&quot;"
		m += "&gt;\n"

	} else if (InputType == "Signature"){
		if (P[5] == "ClassicSignature"){
			m="";
		} else {
			var x0 = parseInt(P[1]);
			var y0 = parseInt(P[2]);
			var width = parseInt(P[3]);
			var height = parseInt(P[4]);
			m ="";
			m +="\t&lt;div id=&quot;Canvas"+P[5]+"&quot; class=&quot;sig&quot; style=&quot;position:absolute; left:";
			m += x0;
			m += "px; top:"
			m += y0;
			m += "px; width:"
			m += width;
			m += "px; height:"
			m += height;
			m += "; z-index:10;&quot;&gt;\n";
	 		m +="\t&lt;/div&gt;\n\n";
			m +="\t&lt;input type=&quot;hidden&quot; name=&quot;Store"+P[5]+"&quot; id=&quot;Store"+P[5]+"&quot; value=&quot;&quot;&gt;\n";
		}

	} else if (InputType == "Stamp"){
		var x0 = parseInt(P[1]);
		var y0 = parseInt(P[2]);
		var width = parseInt(P[3]);
		var height = parseInt(P[4]);
		var inputName = P[5];
		var signo = parseInt(P[6]);
		m = "&lt;div style=&quot;position:absolute; left:" 
		m += x0
		m += "px; top:"
		m += y0
		m += "px;&quot;&gt;\n"
		m += "&lt;img id=&quot;Stamp&quot; src=&quot;../eform/displayImage.do?imagefile=BNK.png&quot; width=&quot;" 
		m += width
		m += "&quot; height=&quot;"
		m += height
		m += "&quot; onclick=&quot;toggleMe();&quot;&gt;\n&lt;/div&gt;\n\n"
	}
	textMiddle += m;
	textMiddle += "\n"
}
function GetTextBottom(){
	//gender checkboxes
	if ((document.getElementById('preCheckGender').checked)||(document.getElementById('XboxType').checked)){
		textBottom += "&lt;input name=&quot;PatientGender&quot; id=&quot;PatientGender&quot; type=&quot;hidden&quot; oscarDB=sex&gt;\n"
	}

	//auto load signature images
	if (document.getElementById('AddStamp').checked){
		textBottom +="&lt;input type=&quot;hidden&quot; name=&quot;DoctorName&quot; id=&quot;DoctorName&quot; oscarDB=doctor&gt;\n"
		textBottom +="&lt;input type=&quot;hidden&quot; name=&quot;CurrentUserName&quot; id=&quot;CurrentUserName&quot; oscarDB=current_user&gt;\n"
		textBottom +="&lt;input type=&quot;hidden&quot; name=&quot;SubmittedBy&quot; id=&quot;SubmittedBy&quot;&gt;\n"
	}

	//classic signature
	if (document.getElementById('AddSignatureClassic').checked){
		textBottom +="&lt;div id=&quot;signatureDisplay&quot;&gt;&lt;/div&gt;&lt;input type=&quot;hidden&quot; name=&quot;signatureValue&quot; id=&quot;signatureValue&quot; value=&quot;&quot; &gt;&lt;/input&gt;\n"	
	}

	//bottom submit boxes
	/*textBottom += "\n\n &lt;div class=&quot;DoNotPrint&quot; id=&quot;BottomButtons&quot; style=&quot;position: relative; top:"
	textBottom += "10px; left:0px;&quot;&gt;\n" */

	textBottom += "\n\n &lt;div class=&quot;DoNotPrint&quot; id=&quot;BottomButtons&quot; style=&quot;position: absolute; top:"

	//var totalpx =  parseInt(BGHeight) ;
	//if ( totalpx == 750 ) { totalpx=1000;} // deep choclate fudge
	//if ( parseInt(pageoffset) == 750 ) { pageoffset=1000;} // more brown fudge
	//totalpx =  totalpx + parseInt(pageoffset);
	//textBottom += totalpx;
	textBottom += pageoffset;
	textBottom += "px; left:0px;&quot;&gt;\n"

	textBottom += "\t &lt;table&gt;&lt;tr&gt;&lt;td&gt;\n"
	textBottom += "\t\t Subject: &lt;input name=&quot;subject&quot; size=&quot;40&quot; type=&quot;text&quot;&gt; &lt;br&gt; \n"
	textBottom += "\t\t&lt;input value=&quot;Submit&quot; name=&quot;SubmitButton&quot; id=&quot;SubmitButton&quot; type=&quot;submit&quot; onclick=&quot;"

	if (document.getElementById('AddSignature').checked){
		textBottom += " saveSig(); releaseDirtyFlag();&quot;&gt; \n"
		textBottom += "\t\t&lt;input value=&quot;Clear Sig&quot; name=&quot;ClearButton&quot; id=&quot;ClearButton&quot; type=&quot;button&quot; onclick=&quot;clearSig();&quot;&gt; \n"
	} else {
		textBottom += " releaseDirtyFlag();&quot;&gt; \n"
	}
	textBottom += "\t\t\t&lt;input value=&quot;Reset&quot; name=&quot;ResetButton&quot; id=&quot;ResetButton&quot; type=&quot;reset&quot;&gt; \n"
	textBottom += "\t\t	&lt;input value=&quot;Print&quot; name=&quot;PrintButton&quot; id=&quot;PrintButton&quot; type=&quot;button&quot; onclick=&quot;formPrint();&quot;&gt; \n"
	textBottom += "\t\t	&lt;input value=&quot;Print &amp; Submit&quot; name=&quot;PrintSubmitButton&quot; id=&quot;PrintSubmitButton&quot; type=&quot;button&quot; onclick=&quot;formPrint();releaseDirtyFlag();setTimeout('SubmitButton.click()',1000);&quot;&gt; \n"

	textBottom += "\t &lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;\n"
	textBottom += " &lt;/div&gt;\n"
	//close the last page here
	textBottom += "&lt;/div&gt;\n"
	textBottom += " &lt;/form&gt;\n\n"

	if (document.getElementById('AddDate').checked){
		textBottom +="\n&lt;!-- Define Date Calendars --&gt;\n"
		textBottom += "&lt;script type=&quot;text/javascript&quot;&gt;\n"
		for (j=0; (j < (DrawData.length) ); j++){
			var P = DrawData[j].split("|");
			if ((P[0]=="Text") && ((P[5].indexOf("today") >-1)||(P[5].indexOf("date") >-1)))  {
				textBottom += "\tCalendar.setup( { inputField : &quot;"+P[5]+"&quot;, ifFormat : &quot;%Y-%m-%d&quot;,  button : &quot;"+P[5]+"&quot; } );\n"		
			}
		}
		textBottom += "&lt;/script&gt;\n\n"
	}

	//</body></html>
	textBottom += "&lt;/body&gt;\n&lt;/html&gt;\n";
}

//load generated eform code in new window
function popUp(){

textTop = "";
GetTextTop();

textMiddle = "";
var m = ""
for (j=0; (j < (DrawData.length) ); j++){
		var GetTextMiddleParameter = DrawData[j].split("|");
		GetTextMiddle(GetTextMiddleParameter);
	}

//PHC edit//

textBottom = "";
GetTextBottom();

text = textTop  + textMiddle + textBottom;

//popup modified at this point PHC
return unescape(text);
}

//this function used for injecting html in to Edit E-Form in efmformmanageredit.jsp w/ variable formHtml
function injectHtml(){
    document.getElementById('formHtmlG').value = popUp();
    document.getElementById('toSave').submit();
}

</script>
<!-- back to boilerplate -->

<!-- mousefunction.js -->
<script type="text/javascript">
var mousex = 0;
var mousey = 0;
var grabx = 0;
var graby = 0;
var orix = 0;
var oriy = 0;
var elex = 0;
var eley = 0;
var algor = 0;

var dragobj = null;

function falsefunc() { return false; } // used to block cascading events

function init()
{
  document.onmousemove = update; // update(event) implied on NS, update(null) implied on IE
  update();
}

function getMouseXY(e) // works on IE6,FF,Moz,Opera7
{
  if (!e) e = window.event; // works on IE, but not NS (we rely on NS passing us the event)

  if (e)
  {
    if (e.pageX || e.pageY)
    { // this doesn't work on IE6!! (works on FF,Moz,Opera7)
      mousex = e.pageX;
      mousey = e.pageY;
      algor = '[e.pageX]';
      if (e.clientX || e.clientY) algor += ' [e.clientX] '
    }
    else if (e.clientX || e.clientY)
    { // works on IE6,FF,Moz,Opera7
      // Note: I am adding together both the "body" and "documentElement" scroll positions
      //       this lets me cover for the quirks that happen based on the "doctype" of the html page.
      //         (example: IE6 in compatibility mode or strict)
      //       Based on the different ways that IE,FF,Moz,Opera use these ScrollValues for body and documentElement
      //       it looks like they will fill EITHER ONE SCROLL VALUE OR THE OTHER, NOT BOTH
      //         (from info at http://www.quirksmode.org/js/doctypes.html)
      mousex = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
      mousey = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
      algor = '[e.clientX]';
      if (e.pageX || e.pageY) algor += ' [e.pageX] '
    }
    if (e.preventDefault) {e.preventDefault();} else {window.event.returnValue = false;window.event.cancelBubble = true}
  }
}

function grab(context)
{
  document.onmousedown = falsefunc; // in NS this prevents cascading of events, thus disabling text selection
  dragobj = context;
  dragobj.style.zIndex = 10; // move it to the top
  document.onmousemove = drag;
  document.onmouseup = drop;
  grabx = mousex;
  graby = mousey;
  elex = orix = dragobj.offsetLeft;
  eley = oriy = dragobj.offsetTop;
  update();
}

function drag(e) // parameter passing is important for NS family
{
  if (dragobj)
  {
    elex = orix + (mousex-grabx);
    eley = oriy + (mousey-graby);
    dragobj.style.position = "absolute";
    dragobj.style.left = (elex).toString(10) + 'px';
    dragobj.style.top  = (eley).toString(10) + 'px';
  }
  update(e);
  return false; // in IE this prevents cascading of events, thus text selection is disabled
}

function drop()
{
	if (dragobj)
	{
		dragobj.style.zIndex = 0;
		dragobj = null;
	}
	update();
	document.onmousemove = update;
	document.onmouseup = null;
	document.onmousedown = null;   // re-enables text selection on NS

}

function update(e)
{
	  getMouseXY(e); // NS is passing (event), while IE is passing (null)
}

</script>

<!-- js graphics scripts -->
<script type="text/javascript" src= "<%=request.getContextPath()%>/share/javascript/eforms/jsgraphics.js" ></script>

<!-- 
<script type="text/javascript">
/* This notice must be untouched at all times.

wz_jsgraphics.js    v. 3.05
The latest version is available at
http://www.walterzorn.com
or http://www.devira.com
or http://www.walterzorn.de

Copyright (c) 2002-2009 Walter Zorn. All rights reserved.
Created 3. 11. 2002 by Walter Zorn (Web: http://www.walterzorn.com )
Last modified: 2. 2. 2009

Performance optimizations for Internet Explorer
by Thomas Frank and John Holdsworth.
fillPolygon method implemented by Matthieu Haller.

High Performance JavaScript Graphics Library.
Provides methods
- to draw lines, rectangles, ellipses, polygons
	with specifiable line thickness,
- to fill rectangles, polygons, ellipses and arcs
- to draw text.
NOTE: Operations, functions and branching have rather been optimized
to efficiency and speed than to shortness of source code.

LICENSE: LGPL

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License (LGPL) as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA,
or see http://www.gnu.org/copyleft/lesser.html
*/


var jg_ok, jg_ie, jg_fast, jg_dom, jg_moz;


function _chkDHTM(wnd, x, i)
// Under XUL, owner of 'document' must be specified explicitly
{
	x = wnd.document.body || null;
	jg_ie = x && typeof x.insertAdjacentHTML != "undefined" && wnd.document.createElement;
	jg_dom = (x && !jg_ie &&
		typeof x.appendChild != "undefined" &&
		typeof wnd.document.createRange != "undefined" &&
		typeof (i = wnd.document.createRange()).setStartBefore != "undefined" &&
		typeof i.createContextualFragment != "undefined");
	jg_fast = jg_ie && wnd.document.all && !wnd.opera;
	jg_moz = jg_dom && typeof x.style.MozOpacity != "undefined";
	jg_ok = !!(jg_ie || jg_dom);
}

function _pntCnvDom()
{
	var x = this.wnd.document.createRange();
	x.setStartBefore(this.cnv);
	x = x.createContextualFragment(jg_fast? this._htmRpc() : this.htm);
	if(this.cnv) this.cnv.appendChild(x);
	this.htm = "";
}

function _pntCnvIe()
{
	if(this.cnv) this.cnv.insertAdjacentHTML("BeforeEnd", jg_fast? this._htmRpc() : this.htm);
	this.htm = "";
}

function _pntDoc()
{
	this.wnd.document.write(jg_fast? this._htmRpc() : this.htm);
	this.htm = '';
}

function _pntN()
{
	;
}

function _mkDiv(x, y, w, h)
{
	this.htm += '<div style="position:absolute;'+
		'left:' + x + 'px;'+
		'top:' + y + 'px;'+
		'width:' + w + 'px;'+
		'height:' + h + 'px;'+
		'clip:rect(0,'+w+'px,'+h+'px,0);'+
		'background-color:' + this.color +
		(!jg_moz? ';overflow:hidden' : '')+
		';"><\/div>';
}

function _mkDivIe(x, y, w, h)
{
	this.htm += '%%'+this.color+';'+x+';'+y+';'+w+';'+h+';';
}

function _mkDivPrt(x, y, w, h)
{
	this.htm += '<div style="position:absolute;'+
		'border-left:' + w + 'px solid ' + this.color + ';'+
		'left:' + x + 'px;'+
		'top:' + y + 'px;'+
		'width:0px;'+
		'height:' + h + 'px;'+
		'clip:rect(0,'+w+'px,'+h+'px,0);'+
		'background-color:' + this.color +
		(!jg_moz? ';overflow:hidden' : '')+
		';"><\/div>';
}

var _regex =  /%%([^;]+);([^;]+);([^;]+);([^;]+);([^;]+);/g;
function _htmRpc()
{
	return this.htm.replace(
		_regex,
		'<div style="overflow:hidden;position:absolute;background-color:'+
		'$1;left:$2px;top:$3px;width:$4px;height:$5px"></div>\n');
}

function _htmPrtRpc()
{
	return this.htm.replace(
		_regex,
		'<div style="overflow:hidden;position:absolute;background-color:'+
		'$1;left:$2px;top:$3px;width:$4px;height:$5px;border-left:$4px solid $1"></div>\n');
}

function _mkLin(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1;

	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx,
		ox = x;
		while(dx > 0)
		{--dx;
			++x;
			if(p > 0)
			{
				this._mkDiv(ox, y, x-ox, 1);
				y += yIncr;
				p += pru;
				ox = x;
			}
			else p += pr;
		}
		this._mkDiv(ox, y, x2-ox+1, 1);
	}

	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy,
		oy = y;
		if(y2 <= y1)
		{
			while(dy > 0)
			{--dy;
				if(p > 0)
				{
					this._mkDiv(x++, y, 1, oy-y+1);
					y += yIncr;
					p += pru;
					oy = y;
				}
				else
				{
					y += yIncr;
					p += pr;
				}
			}
			this._mkDiv(x2, y2, 1, oy-y2+1);
		}
		else
		{
			while(dy > 0)
			{--dy;
				y += yIncr;
				if(p > 0)
				{
					this._mkDiv(x++, oy, 1, y-oy);
					p += pru;
					oy = y;
				}
				else p += pr;
			}
			this._mkDiv(x2, oy, 1, y2-oy+1);
		}
	}
}

function _mkLin2D(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1;

	var s = this.stroke;
	if(dx >= dy)
	{
		if(dx > 0 && s-3 > 0)
		{
			var _s = (s*dx*Math.sqrt(1+dy*dy/(dx*dx))-dx-(s>>1)*dy) / dx;
			_s = (!(s-4)? Math.ceil(_s) : Math.round(_s)) + 1;
		}
		else var _s = s;
		var ad = Math.ceil(s/2);

		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx,
		ox = x;
		while(dx > 0)
		{--dx;
			++x;
			if(p > 0)
			{
				this._mkDiv(ox, y, x-ox+ad, _s);
				y += yIncr;
				p += pru;
				ox = x;
			}
			else p += pr;
		}
		this._mkDiv(ox, y, x2-ox+ad+1, _s);
	}

	else
	{
		if(s-3 > 0)
		{
			var _s = (s*dy*Math.sqrt(1+dx*dx/(dy*dy))-(s>>1)*dx-dy) / dy;
			_s = (!(s-4)? Math.ceil(_s) : Math.round(_s)) + 1;
		}
		else var _s = s;
		var ad = Math.round(s/2);

		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy,
		oy = y;
		if(y2 <= y1)
		{
			++ad;
			while(dy > 0)
			{--dy;
				if(p > 0)
				{
					this._mkDiv(x++, y, _s, oy-y+ad);
					y += yIncr;
					p += pru;
					oy = y;
				}
				else
				{
					y += yIncr;
					p += pr;
				}
			}
			this._mkDiv(x2, y2, _s, oy-y2+ad);
		}
		else
		{
			while(dy > 0)
			{--dy;
				y += yIncr;
				if(p > 0)
				{
					this._mkDiv(x++, oy, _s, y-oy+ad);
					p += pru;
					oy = y;
				}
				else p += pr;
			}
			this._mkDiv(x2, oy, _s, y2-oy+ad+1);
		}
	}
}

function _mkLinDott(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1,
	drw = true;
	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx;
		while(dx > 0)
		{--dx;
			if(drw) this._mkDiv(x, y, 1, 1);
			drw = !drw;
			if(p > 0)
			{
				y += yIncr;
				p += pru;
			}
			else p += pr;
			++x;
		}
	}
	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy;
		while(dy > 0)
		{--dy;
			if(drw) this._mkDiv(x, y, 1, 1);
			drw = !drw;
			y += yIncr;
			if(p > 0)
			{
				++x;
				p += pru;
			}
			else p += pr;
		}
	}
	if(drw) this._mkDiv(x, y, 1, 1);
}

function _mkOv(left, top, width, height)
{
	var a = (++width)>>1, b = (++height)>>1,
	wod = width&1, hod = height&1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	ox = 0, oy = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1),
	w, h;
	while(y > 0)
	{
		if(st < 0)
		{
			st += bb2*((x<<1)+3);
			tt += bb4*(++x);
		}
		else if(tt < 0)
		{
			st += bb2*((x<<1)+3) - aa4*(y-1);
			tt += bb4*(++x) - aa2*(((y--)<<1)-3);
			w = x-ox;
			h = oy-y;
			if((w&2) && (h&2))
			{
				this._mkOvQds(cx, cy, x-2, y+2, 1, 1, wod, hod);
				this._mkOvQds(cx, cy, x-1, y+1, 1, 1, wod, hod);
			}
			else this._mkOvQds(cx, cy, x-1, oy, w, h, wod, hod);
			ox = x;
			oy = y;
		}
		else
		{
			tt -= aa2*((y<<1)-3);
			st -= aa4*(--y);
		}
	}
	w = a-ox+1;
	h = (oy<<1)+hod;
	y = cy-oy;
	this._mkDiv(cx-a, y, w, h);
	this._mkDiv(cx+ox+wod-1, y, w, h);
}

function _mkOv2D(left, top, width, height)
{
	var s = this.stroke;
	width += s+1;
	height += s+1;
	var a = width>>1, b = height>>1,
	wod = width&1, hod = height&1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1);

	if(s-4 < 0 && (!(s-2) || width-51 > 0 && height-51 > 0))
	{
		var ox = 0, oy = b,
		w, h,
		pxw;
		while(y > 0)
		{
			if(st < 0)
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				w = x-ox;
				h = oy-y;

				if(w-1)
				{
					pxw = w+1+(s&1);
					h = s;
				}
				else if(h-1)
				{
					pxw = s;
					h += 1+(s&1);
				}
				else pxw = h = s;
				this._mkOvQds(cx, cy, x-1, oy, pxw, h, wod, hod);
				ox = x;
				oy = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}
		}
		this._mkDiv(cx-a, cy-oy, s, (oy<<1)+hod);
		this._mkDiv(cx+a+wod-s, cy-oy, s, (oy<<1)+hod);
	}

	else
	{
		var _a = (width-(s<<1))>>1,
		_b = (height-(s<<1))>>1,
		_x = 0, _y = _b,
		_aa2 = (_a*_a)<<1, _aa4 = _aa2<<1, _bb2 = (_b*_b)<<1, _bb4 = _bb2<<1,
		_st = (_aa2>>1)*(1-(_b<<1)) + _bb2,
		_tt = (_bb2>>1) - _aa2*((_b<<1)-1),

		pxl = new Array(),
		pxt = new Array(),
		_pxb = new Array();
		pxl[0] = 0;
		pxt[0] = b;
		_pxb[0] = _b-1;
		while(y > 0)
		{
			if(st < 0)
			{
				pxl[pxl.length] = x;
				pxt[pxt.length] = y;
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				pxl[pxl.length] = x;
				st += bb2*((x<<1)+3) - aa4*(y-1);
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				pxt[pxt.length] = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}

			if(_y > 0)
			{
				if(_st < 0)
				{
					_st += _bb2*((_x<<1)+3);
					_tt += _bb4*(++_x);
					_pxb[_pxb.length] = _y-1;
				}
				else if(_tt < 0)
				{
					_st += _bb2*((_x<<1)+3) - _aa4*(_y-1);
					_tt += _bb4*(++_x) - _aa2*(((_y--)<<1)-3);
					_pxb[_pxb.length] = _y-1;
				}
				else
				{
					_tt -= _aa2*((_y<<1)-3);
					_st -= _aa4*(--_y);
					_pxb[_pxb.length-1]--;
				}
			}
		}

		var ox = -wod, oy = b,
		_oy = _pxb[0],
		l = pxl.length,
		w, h;
		for(var i = 0; i < l; i++)
		{
			if(typeof _pxb[i] != "undefined")
			{
				if(_pxb[i] < _oy || pxt[i] < oy)
				{
					x = pxl[i];
					this._mkOvQds(cx, cy, x, oy, x-ox, oy-_oy, wod, hod);
					ox = x;
					oy = pxt[i];
					_oy = _pxb[i];
				}
			}
			else
			{
				x = pxl[i];
				this._mkDiv(cx-x, cy-oy, 1, (oy<<1)+hod);
				this._mkDiv(cx+ox+wod, cy-oy, 1, (oy<<1)+hod);
				ox = x;
				oy = pxt[i];
			}
		}
		this._mkDiv(cx-a, cy-oy, 1, (oy<<1)+hod);
		this._mkDiv(cx+ox+wod, cy-oy, 1, (oy<<1)+hod);
	}
}

function _mkOvDott(left, top, width, height)
{
	var a = (++width)>>1, b = (++height)>>1,
	wod = width&1, hod = height&1, hodu = hod^1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1),
	drw = true;
	while(y > 0)
	{
		if(st < 0)
		{
			st += bb2*((x<<1)+3);
			tt += bb4*(++x);
		}
		else if(tt < 0)
		{
			st += bb2*((x<<1)+3) - aa4*(y-1);
			tt += bb4*(++x) - aa2*(((y--)<<1)-3);
		}
		else
		{
			tt -= aa2*((y<<1)-3);
			st -= aa4*(--y);
		}
		if(drw && y >= hodu) this._mkOvQds(cx, cy, x, y, 1, 1, wod, hod);
		drw = !drw;
	}
}

function _mkRect(x, y, w, h)
{
	var s = this.stroke;
	this._mkDiv(x, y, w, s);
	this._mkDiv(x+w, y, s, h);
	this._mkDiv(x, y+h, w+s, s);
	this._mkDiv(x, y+s, s, h-s);
}

function _mkRectDott(x, y, w, h)
{
	this.drawLine(x, y, x+w, y);
	this.drawLine(x+w, y, x+w, y+h);
	this.drawLine(x, y+h, x+w, y+h);
	this.drawLine(x, y, x, y+h);
}

function jsgFont()
{
	this.PLAIN = 'font-weight:normal;';
	this.BOLD = 'font-weight:bold;';
	this.ITALIC = 'font-style:italic;';
	this.ITALIC_BOLD = this.ITALIC + this.BOLD;
	this.BOLD_ITALIC = this.ITALIC_BOLD;
}
var Font = new jsgFont();

function jsgStroke()
{
	this.DOTTED = -1;
}
var Stroke = new jsgStroke();

function jsGraphics(cnv, wnd)
{
	this.setColor = function(x)
	{
		this.color = x.toLowerCase();
	};

	this.setStroke = function(x)
	{
		this.stroke = x;
		if(!(x+1))
		{
			this.drawLine = _mkLinDott;
			this._mkOv = _mkOvDott;
			this.drawRect = _mkRectDott;
		}
		else if(x-1 > 0)
		{
			this.drawLine = _mkLin2D;
			this._mkOv = _mkOv2D;
			this.drawRect = _mkRect;
		}
		else
		{
			this.drawLine = _mkLin;
			this._mkOv = _mkOv;
			this.drawRect = _mkRect;
		}
	};

	this.setPrintable = function(arg)
	{
		this.printable = arg;
		if(jg_fast)
		{
			this._mkDiv = _mkDivIe;
			this._htmRpc = arg? _htmPrtRpc : _htmRpc;
		}
		else this._mkDiv = arg? _mkDivPrt : _mkDiv;
	};

	this.setFont = function(fam, sz, sty)
	{
		this.ftFam = fam;
		this.ftSz = sz;
		this.ftSty = sty || Font.PLAIN;
	};

	this.drawPolyline = this.drawPolyLine = function(x, y)
	{
		for (var i=x.length - 1; i;)
		{--i;
			this.drawLine(x[i], y[i], x[i+1], y[i+1]);
		}
	};

	this.fillRect = function(x, y, w, h)
	{
		this._mkDiv(x, y, w, h);
	};

	this.drawPolygon = function(x, y)
	{
		this.drawPolyline(x, y);
		this.drawLine(x[x.length-1], y[x.length-1], x[0], y[0]);
	};

	this.drawEllipse = this.drawOval = function(x, y, w, h)
	{
		this._mkOv(x, y, w, h);
	};

	this.fillEllipse = this.fillOval = function(left, top, w, h)
	{
		var a = w>>1, b = h>>1,
		wod = w&1, hod = h&1,
		cx = left+a, cy = top+b,
		x = 0, y = b, oy = b,
		aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
		st = (aa2>>1)*(1-(b<<1)) + bb2,
		tt = (bb2>>1) - aa2*((b<<1)-1),
		xl, dw, dh;
		if(w) while(y > 0)
		{
			if(st < 0)
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				xl = cx-x;
				dw = (x<<1)+wod;
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				dh = oy-y;
				this._mkDiv(xl, cy-oy, dw, dh);
				this._mkDiv(xl, cy+y+hod, dw, dh);
				oy = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}
		}
		this._mkDiv(cx-a, cy-oy, w, (oy<<1)+hod);
	};

	this.fillArc = function(iL, iT, iW, iH, fAngA, fAngZ)
	{
		var a = iW>>1, b = iH>>1,
		iOdds = (iW&1) | ((iH&1) << 16),
		cx = iL+a, cy = iT+b,
		x = 0, y = b, ox = x, oy = y,
		aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
		st = (aa2>>1)*(1-(b<<1)) + bb2,
		tt = (bb2>>1) - aa2*((b<<1)-1),
		// Vars for radial boundary lines
		xEndA, yEndA, xEndZ, yEndZ,
		iSects = (1 << (Math.floor((fAngA %= 360.0)/180.0) << 3))
				| (2 << (Math.floor((fAngZ %= 360.0)/180.0) << 3))
				| ((fAngA >= fAngZ) << 16),
		aBndA = new Array(b+1), aBndZ = new Array(b+1);
		
		// Set up radial boundary lines
		fAngA *= Math.PI/180.0;
		fAngZ *= Math.PI/180.0;
		xEndA = cx+Math.round(a*Math.cos(fAngA));
		yEndA = cy+Math.round(-b*Math.sin(fAngA));
		_mkLinVirt(aBndA, cx, cy, xEndA, yEndA);
		xEndZ = cx+Math.round(a*Math.cos(fAngZ));
		yEndZ = cy+Math.round(-b*Math.sin(fAngZ));
		_mkLinVirt(aBndZ, cx, cy, xEndZ, yEndZ);

		while(y > 0)
		{
			if(st < 0) // Advance x
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0) // Advance x and y
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				ox = x;
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				this._mkArcDiv(ox, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
				oy = y;
			}
			else // Advance y
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
				if(y && (aBndA[y] != aBndA[y-1] || aBndZ[y] != aBndZ[y-1]))
				{
					this._mkArcDiv(x, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
					ox = x;
					oy = y;
				}
			}
		}
		this._mkArcDiv(x, 0, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
		if(iOdds >> 16) // Odd height
		{
			if(iSects >> 16) // Start-angle > end-angle
			{
				var xl = (yEndA <= cy || yEndZ > cy)? (cx - x) : cx;
				this._mkDiv(xl, cy, x + cx - xl + (iOdds & 0xffff), 1);
			}
			else if((iSects & 0x01) && yEndZ > cy)
				this._mkDiv(cx - x, cy, x, 1);
		}
	};

/* fillPolygon method, implemented by Matthieu Haller.
This javascript function is an adaptation of the gdImageFilledPolygon for Walter Zorn lib.
C source of GD 1.8.4 found at http://www.boutell.com/gd/

THANKS to Kirsten Schulz for the polygon fixes!

The intersection finding technique of this code could be improved
by remembering the previous intertersection, and by using the slope.
That could help to adjust intersections to produce a nice
interior_extrema. */
	this.fillPolygon = function(array_x, array_y)
	{
		var i;
		var y;
		var miny, maxy;
		var x1, y1;
		var x2, y2;
		var ind1, ind2;
		var ints;

		var n = array_x.length;
		if(!n) return;

		miny = array_y[0];
		maxy = array_y[0];
		for(i = 1; i < n; i++)
		{
			if(array_y[i] < miny)
				miny = array_y[i];

			if(array_y[i] > maxy)
				maxy = array_y[i];
		}
		for(y = miny; y <= maxy; y++)
		{
			var polyInts = new Array();
			ints = 0;
			for(i = 0; i < n; i++)
			{
				if(!i)
				{
					ind1 = n-1;
					ind2 = 0;
				}
				else
				{
					ind1 = i-1;
					ind2 = i;
				}
				y1 = array_y[ind1];
				y2 = array_y[ind2];
				if(y1 < y2)
				{
					x1 = array_x[ind1];
					x2 = array_x[ind2];
				}
				else if(y1 > y2)
				{
					y2 = array_y[ind1];
					y1 = array_y[ind2];
					x2 = array_x[ind1];
					x1 = array_x[ind2];
				}
				else continue;

				 //  Modified 11. 2. 2004 Walter Zorn
				if((y >= y1) && (y < y2))
					polyInts[ints++] = Math.round((y-y1) * (x2-x1) / (y2-y1) + x1);

				else if((y == maxy) && (y > y1) && (y <= y2))
					polyInts[ints++] = Math.round((y-y1) * (x2-x1) / (y2-y1) + x1);
			}
			polyInts.sort(_CompInt);
			for(i = 0; i < ints; i+=2)
				this._mkDiv(polyInts[i], y, polyInts[i+1]-polyInts[i]+1, 1);
		}
	};

	this.drawString = function(txt, x, y)
	{
		this.htm += '<div style="position:absolute;white-space:nowrap;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			'font-family:' +  this.ftFam + ';'+
			'font-size:' + this.ftSz + ';'+
			'color:' + this.color + ';' + this.ftSty + '">'+
			txt +
			'<\/div>';
	};

/* drawStringRect() added by Rick Blommers.
Allows to specify the size of the text rectangle and to align the
text both horizontally (e.g. right) and vertically within that rectangle */
	this.drawStringRect = function(txt, x, y, width, halign)
	{
		this.htm += '<div style="position:absolute;overflow:hidden;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			'width:'+width +'px;'+
			'text-align:'+halign+';'+
			'font-family:' +  this.ftFam + ';'+
			'font-size:' + this.ftSz + ';'+
			'color:' + this.color + ';' + this.ftSty + '">'+
			txt +
			'<\/div>';
	};

	this.drawImage = function(imgSrc, x, y, w, h, a)
	{
		this.htm += '<div style="position:absolute;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			// w (width) and h (height) arguments are now optional.
			// Added by Mahmut Keygubatli, 14.1.2008
			(w? ('width:' +  w + 'px;') : '') +
			(h? ('height:' + h + 'px;'):'')+'">'+
			'<img src="' + imgSrc +'"'+ (w ? (' width="' + w + '"'):'')+ (h ? (' height="' + h + '"'):'') + (a? (' '+a) : '') + '>'+
			'<\/div>';
	};

	this.clear = function()
	{
		this.htm = "";
		if(this.cnv) this.cnv.innerHTML = "";
	};

	this._mkOvQds = function(cx, cy, x, y, w, h, wod, hod)
	{
		var xl = cx - x, xr = cx + x + wod - w, yt = cy - y, yb = cy + y + hod - h;
		if(xr > xl+w)
		{
			this._mkDiv(xr, yt, w, h);
			this._mkDiv(xr, yb, w, h);
		}
		else
			w = xr - xl + w;
		this._mkDiv(xl, yt, w, h);
		this._mkDiv(xl, yb, w, h);
	};
	
	this._mkArcDiv = function(x, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects)
	{
		var xrDef = cx + x + (iOdds & 0xffff), y2, h = oy - y, xl, xr, w;

		if(!h) h = 1;
		x = cx - x;

		if(iSects & 0xff0000) // Start-angle > end-angle
		{
			y2 = cy - y - h;
			if(iSects & 0x00ff)
			{
				if(iSects & 0x02)
				{
					xl = Math.max(x, aBndZ[y]);
					w = xrDef - xl;
					if(w > 0) this._mkDiv(xl, y2, w, h);
				}
				if(iSects & 0x01)
				{
					xr = Math.min(xrDef, aBndA[y]);
					w = xr - x;
					if(w > 0) this._mkDiv(x, y2, w, h);
				}
			}
			else
				this._mkDiv(x, y2, xrDef - x, h);
			y2 = cy + y + (iOdds >> 16);
			if(iSects & 0xff00)
			{
				if(iSects & 0x0100)
				{
					xl = Math.max(x, aBndA[y]);
					w = xrDef - xl;
					if(w > 0) this._mkDiv(xl, y2, w, h);
				}
				if(iSects & 0x0200)
				{
					xr = Math.min(xrDef, aBndZ[y]);
					w = xr - x;
					if(w > 0) this._mkDiv(x, y2, w, h);
				}
			}
			else
				this._mkDiv(x, y2, xrDef - x, h);
		}
		else
		{
			if(iSects & 0x00ff)
			{
				if(iSects & 0x02)
					xl = Math.max(x, aBndZ[y]);
				else
					xl = x;
				if(iSects & 0x01)
					xr = Math.min(xrDef, aBndA[y]);
				else
					xr = xrDef;
				y2 = cy - y - h;
				w = xr - xl;
				if(w > 0) this._mkDiv(xl, y2, w, h);
			}
			if(iSects & 0xff00)
			{
				if(iSects & 0x0100)
					xl = Math.max(x, aBndA[y]);
				else
					xl = x;
				if(iSects & 0x0200)
					xr = Math.min(xrDef, aBndZ[y]);
				else
					xr = xrDef;
				y2 = cy + y + (iOdds >> 16);
				w = xr - xl;
				if(w > 0) this._mkDiv(xl, y2, w, h);
			}
		}
	};

	this.setStroke(1);
	this.setFont("verdana,geneva,helvetica,sans-serif", "12px", Font.PLAIN);
	this.color = "#000000";
	this.htm = "";
	this.wnd = wnd || window;

	if(!jg_ok) _chkDHTM(this.wnd);
	if(jg_ok)
	{
		if(cnv)
		{
			if(typeof(cnv) == "string")
				this.cont = document.all? (this.wnd.document.all[cnv] || null)
					: document.getElementById? (this.wnd.document.getElementById(cnv) || null)
					: null;
			else if(cnv == window.document)
				this.cont = document.getElementsByTagName("body")[0];
			// If cnv is a direct reference to a canvas DOM node
			// (option suggested by Andreas Luleich)
			else this.cont = cnv;
			// Create new canvas inside container DIV. Thus the drawing and clearing
			// methods won't interfere with the container's inner html.
			// Solution suggested by Vladimir.
			this.cnv = this.wnd.document.createElement("div");
			this.cnv.style.fontSize=0;
			this.cont.appendChild(this.cnv);
			this.paint = jg_dom? _pntCnvDom : _pntCnvIe;
		}
		else
			this.paint = _pntDoc;
	}
	else
		this.paint = _pntN;

	this.setPrintable(false);
}

function _mkLinVirt(aLin, x1, y1, x2, y2)
{
	var dx = Math.abs(x2-x1), dy = Math.abs(y2-y1),
	x = x1, y = y1,
	xIncr = (x1 > x2)? -1 : 1,
	yIncr = (y1 > y2)? -1 : 1,
	p,
	i = 0;
	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1);
		p = pr-dx;
		while(dx > 0)
		{--dx;
			if(p > 0)    //  Increment y
			{
				aLin[i++] = x;
				y += yIncr;
				p += pru;
			}
			else p += pr;
			x += xIncr;
		}
	}
	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1);
		p = pr-dy;
		while(dy > 0)
		{--dy;
			y += yIncr;
			aLin[i++] = x;
			if(p > 0)    //  Increment x
			{
				x += xIncr;
				p += pru;
			}
			else p += pr;
		}
	}
	for(var len = aLin.length, i = len-i; i;)
		aLin[len-(i--)] = x;
};

function _CompInt(x, y)
{
	return(x - y);
}


</script>
-->


</head>

<!-- resetAll() -->
<body onload="init(); resetAll(); hide('all');
<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_signature_enabled")) { %>
show('classic');
<% } %> ">

<img name="BGImage" id="BGImage" style="position: absolute; left: 0px; top: 0px;"
	onmouseover="SetDrawOn();"
	onmouseout="SetDrawOff();"
	onmousedown="if (event.preventDefault) event.preventDefault(); SetMouseDown();SetStart();"
	onmousemove=""
	onmouseup="SetMouseUp(); DrawMarker();loadInputList();"
	onload="finishLoadingImage()">

<h1><bean:message key="eFormGenerator.title"/></h1>

<!-- this form  used for injecting html in to Edit E-Form  efmformmanageredit.jsp -->
<form method="post" action="efmformmanageredit.jsp" id="toSave">
    <input type="hidden" name="formHtmlG" id="formHtmlG" />
</form>

<form method="post" action="" name="generator" id="generator">

<div name="Wizard" id="Wizard" class="DoNotPrint" style="position: absolute; left:750px; top: 0px; width: 500px; padding:5px; height: 1010; overflow-y: scroll;" >

<span class="h1"><bean:message key="eFormGenerator.title"/></span>
	<a onclick="show('all');"><bean:message key="eFormGenerator.expandAll"/></a>/
	<a onclick="hide('all');"><bean:message key="eFormGenerator.collapseAll"/></a>

<hr>
<span class="h2">1. <bean:message key="eFormGenerator.loadImage"/>:</span> <a onclick="show('Section1');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section1');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section1">
 <p><select name="imageName" id="imageName">
                 <option value=""                    ><bean:message key="eFormGenerator.imageChooseSelect"/>...</option>
                    <%
                    /**
                        this function/scriplet look in images directory and populate the selection
                        so that the user can select which image they want to use for generating an eform
                    */
                    String imagePath = OscarProperties.getInstance().getProperty("eform_image");
                    if (imagePath == null) { 
			MiscUtils.getLogger().debug("Please provide a valid image path for eform_image in properties");  
			}
                    String[] fileINames = new File(imagePath).list();
                    if (fileINames == null) { 
			MiscUtils.getLogger().debug("Strange, no files found in the supplied eform_image directory");  
			}
                    Arrays.sort(fileINames);

                    for (int i = 0; i < fileINames.length; i++) {  %>
                       <option value="<%= fileINames[i] %>"  ><%= fileINames[i] %></option>

                       <%
                      }
                     %>
            </select> <bean:message key="eFormGenerator.page"/> <input type="text" name="page" id="page" style="width:30px" value="" readonly="true">
        </p>

	<!-- <p><b>Image Name:</b><input type="text" name="imageName" id="imageName"></p> -->
	<p>	- <bean:message key="eFormGenerator.imageUploadPrompt"/> <bean:message key="eFormGenerator.imageUploadLink"/> </p>
	<p><b>Orientation of form:</b><br>
			<input type="radio" name="Orientation" id="OrientPortrait" value="750" checked><bean:message key="eFormGenerator.imagePortrait"/><br>
			<input type="radio" name="Orientation" id="OrientLandscape" value="1000"><bean:message key="eFormGenerator.imageLandscape"/><br>
			<input type="radio" name="Orientation" id="OrientCustom" value="CustomWidth"><bean:message key="eFormGenerator.imageCustom"/> <input type="text" name="OrientCustomValue" id="OrientCustomValue" width="100"> <bean:message key="eFormGenerator.imageEnterInteger"/><br>
			<input type="button" value=<bean:message key="eFormGenerator.imageLoadButton"/> onClick="loadImage();">
	</p>
	<p><bean:message key="eFormGenerator.image.RedOutlinehint"/></p>

</div>

<hr>

<span class='h2'>2. <bean:message key="eFormGenerator.eFormName"/></span> <a onclick="show('Section2');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section2');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section2">
	<p><bean:message key="eFormGenerator.nameInstruction"/><input type="text" name="eFormName" id="eFormName"></p>
</div>

<hr>

<span class='h2'>3. <bean:message key="eFormGenerator.gender"/></span> <a onclick="show('Section3');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section3');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section3">
			<p><bean:message key="eFormGenerator.genderCheckbox"/><br> <input name="preCheckGender" id="preCheckGender" type="checkbox" onclick="toggleView(this.checked,'Section3a');"><bean:message key="eFormGenerator.GenderCheckbox"/></p>
			<p><input name="XboxType" id="XboxType" type="checkbox" onclick="toggleView(this.checked,'Section3a');"><bean:message key="eFormGenerator.GenderXbox"/></p>
			<div id="Section3a" style="display:none">
				<table>
					<tr>
						<td><span><b><bean:message key="eFormGenerator.genderMale"/></b>: </span></td>
						<td><input name="Male" id="Male" type="button" value='<bean:message key="eFormGenerator.genderMaleButton"/>' onclick="SetSwitchOn(this.id);"></td>
					</tr>
					<tr>
						<td><span><b><bean:message key="eFormGenerator.genderFemale"/></b>:</span></td>
						<td><input name="Female" id="Female" type="button" value='<bean:message key="eFormGenerator.genderFemaleButton"/>' onclick="SetSwitchOn(this.id);"></td>
					</tr>
				</table>
			</div>
			<p><bean:message key="eFormGenerator.radio"/><br> <input name="radio" id="radio" type="checkbox" onclick="toggleView(this.checked,'Section3b');"><bean:message key="eFormGenerator.radioCheckbox"/></p>
			<div id="Section3b" style="display:none">
				<span><b><bean:message key="eFormGenerator.radioLabel"/></b>: </span>
				<input name="RadioButton" id="RadioButton" type="button" value='<bean:message key="eFormGenerator.radioButton"/>' onclick="SetSwitchOn(this.id);"></td>
					<br>
				<span><bean:message key="eFormGenerator.radioHint"/><span><input type="text" name="RadioName" id="RadioName" style="width:200px;" value="radio">
			</div>
			<p><bean:message key="eFormGenerator.parent"/><br> <input name="parentchild" id="parentchild" type="checkbox" onclick="toggleView(this.checked,'Section3c');"><bean:message key="eFormGenerator.parentCheckbox"/></p>
			<div id="Section3c" style="display:none">
								<table>
					<tr>
						<td><span><b><bean:message key="eFormGenerator.parentLabel"/></b>: </span></td>
						<td><input name="Parent" id="Parent" type="button" value='<bean:message key="eFormGenerator.parentButton"/>' onclick="parentcounter += 1; document.getElementById('Checkbox').click(); document.getElementById('inputClass').value = 'parent-field'; document.generator.InputNameType[1].checked=true; document.getElementById('inputName').value ='parent' + parentcounter; document.getElementById('inputParentclass').value ='';" ></td>
						
					</tr>
					<tr>
						<td><span><b><bean:message key="eFormGenerator.childLabel"/></b>: </span></td>
						<td><input name="Child" id="Child" type="button" value='<bean:message key="eFormGenerator.childButton"/>' onclick=" document.getElementById('inputClass').value = 'child-'; document.getElementById('inputParentclass').value ='parent' + parentcounter; document.getElementById('InputNameAuto').click();"></td>
						
					</tr>
				</table>
			</div>
</div>
<hr>


<span class='h2'>4. <bean:message key="eFormGenerator.signature"/></span><a onclick="show('Section4');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section4');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section4">
	<p>
		<input type="checkbox" name="AddSignature" id="AddSignature" 
			onclick="	toggleView(this.checked,'Section4a');"><bean:message key="eFormGenerator.freehand"/>
<!-- Add A Freehand Signature area to this form--> <br>
			<div id="Section4a" style="display:none"> 
				<input type="button" name="AddSignatureBox1" id="AddSignatureBox1" style="width:400px" value="<bean:message key="eFormGenerator.signatureLocationButton"/>" onclick="SetSwitchOn('SignatureBox');document.getElementById('AddSignature').disabled=true; ">
				<p>Signature Color
				<select id="sigColor" onchange="SignatureColor=document.getElementById('sigColor').value;">
					<option value="black" selected>black</option>
					<option value="blue">blue</option>
					<option value="green">green</option>
					<option value="red">red</option>
					<option value="orange">orange</option>
					<option value="purple">purple</option>
					<option value="brown">brown</option>
				</select>
Signature Line Color
				<select id="sigLineColor" onchange="SignatureLineColor=document.getElementById('sigLineColor').value;">
					<option value="#FFFFFF" selected>white</option>
					<option value="#000000">black</option>
					<option value="#0000FF">blue</option>
					<option value="#00FF00">green</option>
					<option value="#FF0000">red</option>
				</select><br>
Boundary Type
				<select id="sigBorderType" onchange="SignatureBorder='2px '+document.getElementById('sigBorderType').value+' '+document.getElementById('sigBorderColor').value;">
					<option value="none">none</option>
					<option value="dotted" selected>dotted</option>
					<option value="dashed">dashed</option>
					<option value="solid">solid</option>
					<option value="double">double</option>
					<option value="groove">groove</option>
				</select>
Boundary Color
				<select id="sigBorderColor" onchange="SignatureBorder='2px '+document.getElementById('sigBorderType').value+' '+document.getElementById('sigBorderColor').value;">
					<option value="black">black</option>
					<option value="blue" selected>blue</option>
					<option value="green">green</option>
					<option value="red">red</option>
					<option value="orange">orange</option>
					<option value="yellow">yellow</option>
					<option value="purple">purple</option>
					<option value="brown">brown</option>
				</select><br>
			</div> 


		<input type="checkbox" name="AddStamp" id="AddStamp" 
			onclick="	toggleView(this.checked,'Section4b');toggleView(this.checked,'Section4c');"><span><bean:message key="eFormGenerator.stamp"/></span><br>
			<div id="Section4b" style="display:none">
				<input type="button" name="AddSignatureBox2" id="AddSignatureBox2" style="width:400px" value="Click here, then drag a box around the signature area" 
onclick="SetSwitchOn('Stamp');document.getElementById('AddStamp').disabled=true; document.getElementById('AddSignatureBox2').disabled=true;">
			</div>
			<div id="Section4c" style="display:none">
				<ul>
					<li><bean:message key="eFormGenerator.signatureFragment"/>
						<input type="text" name="UserList" id="UserList" style="width:200px;"></li>
					<li><bean:message key="eFormGenerator.signatureImage"/>
						<input type="text" name="SignatureList" id="SignatureList" style="width:200px;"></li>
					<input type="button" name="AddToUserSignatureList" id="AddToUserSignatureList" value="<bean:message key="eFormGenerator.signatureAddButton"/>" onclick="addToUserSignatureList();">
					<input type="button" name="EmptyUserSignatureList" id="EmptyUserSignatureList" value="<bean:message key="eFormGenerator.signatureEmptyButton"/>" onclick="emptyUserSignaturelist()"><br>
					<ul name="UserSignatureList" id="UserSignatureList" style="list-style-type:none; list-style: none; margin-left: 0; padding-left: 1em; text-indent: -1em">
						<li name="UserSignatureListItem">
						      zapski|PHC.png
						</li><li name="UserSignatureListItem">
						      hurman|MCH.png
						</li><li name="UserSignatureListItem">
						      mith|PJS.png
						</li><li name="UserSignatureListItem">
						      lokod|FAO.png
						</li><li name="UserSignatureListItem">
						      urie|LNC.png
						</li><li name="UserSignatureListItem">
						      esilet|SAD.png
						</li><li name="UserSignatureListItem">
						      lgadi|KME.png
						</li><li name="UserSignatureListItem">
						      ears|STS.png
						</li><li name="UserSignatureListItem">
						      ayes|LH.png
						</li>
					</ul>
				</ul>
			</div>
	</p>

	<span id="classic" style="display:none">
	<p>
		<input type="checkbox" name="AddSignatureClassic" id="AddSignatureClassic" 
			onclick="	toggleView(this.checked,'Section4d');"><bean:message key="eFormGenerator.classic"/>
<br>	</span>
			<div id="Section4d" style="display:none"> 
				<input type="button" name="AddClassicSignatureBox" id="AddClassicSignatureBox" style="width:400px" value="<bean:message key="eFormGenerator.signatureLocationButton"/>" onclick="SetSwitchOn('ClassicSignature');document.getElementById('AddSignatureClassic').disabled=true; document.getElementById('AddClassicSignatureBox').disabled=true;">
			</div> 


</div>

<hr>




<span class='h2'>5. <bean:message key="eFormGenerator.input"/></span> <a onclick="show('Section5');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section5');"><bean:message key="eFormGenerator.collapse"/></a></span>
<div id="Section5">
	<span class='h3'><bean:message key="eFormGenerator.inputType"/></span>
		<p>
		<input type="radio" name="inputType" id="Text" value="text" onclick="hide('SectionPrecheck');show('SectionCustomText');show('SectionDatabase');show('SectionImportMeasurements');show('wtalign');show('SectionCustomText');show('iiimeasures');show('c_formating');SetSwitchOn(this.id);document.getElementById('bgColor').value='transparent';"><bean:message key="eFormGenerator.inputTypeText"/>
		<input type="radio" name="inputType" id="Textbox" value="textarea" onclick="hide('SectionPrecheck');show('SectionCustomText');show('SectionDatabase');show('wtalign');show('SectionCustomText');show('c_formating');SetSwitchOn(this.id);"><bean:message key="eFormGenerator.inputTypeTextArea"/>
		<input type="radio" name="inputType" id="Checkbox" value="checkbox" onclick="show('SectionPrecheck');hide('SectionCustomText');hide('SectionDatabase');hide('SectionImportMeasurements');hide('iiimeasures');hide('c_formating');SetSwitchOn(this.id);"><bean:message key="eFormGenerator.inputTypeCheckbox"/>
		<input type="radio" name="inputType" id="Xbox" value="Xbox" onclick="show('SectionPrecheck');show('SectionCustomText');hide('SectionDatabase');hide('SectionImportMeasurements');hide('wtalign');show('c_formating');hide('iiimeasures');SetSwitchOn(this.id);document.getElementById('bgColor').value='white';"><bean:message key="eFormGenerator.inputTypeXbox"/>
		</p>

	<span class='h3'><bean:message key="eFormGenerator.inputAuto"/></span>
<ul style="list-style-type:none">
			<li id='SectionCustomText' style="display:block">
				<input type="radio" name="AutoPopType" id="AutoPopCustom" value="custom"><bean:message key="eFormGenerator.inputTypeCustom"/><input type="text" name="inputValue" id="inputValue" value=""></li>
			<li id='SectionDatabase' style="display:block">
				<input type="radio" name="AutoPopType" id="AutoPopDatabase" value="database"><bean:message key="eFormGenerator.inputTypeData"/>
			 <select name="oscarDB" id="oscarDB">
                                 <option value=""          ><bean:message key="eFormGenerator.inputTypeDataButton"/></option>
                                <%
                                  EFormLoader names = EFormLoader.getInstance();
                                  //return the array with a list of names from database
                                  List<String> kout = names.getNames();
                                for(String str :kout){ %>
                                  <option value="<%= str %>"  ><%= str %></option>
                                   <%
                                  }
                                 %>
                        </select>			</li>
			<li id="SectionImportMeasurements" style="diplay:block;">
				<input type="radio" name="AutoPopType" id="AutoPopMeasurements" value="measurements"><bean:message key="eFormGenerator.inputTypeMeasurements"/><br>
				<table>
					<tr>
						<td><p><bean:message key="eFormGenerator.inputTypeMeasurementsType"/></p></td>
						<td><p>
							<select name="MeasurementList" id="MeasurementList">
								<option value="" selected="selected"><bean:message key="eFormGenerator.inputTypeMeasurementsButton"/></option>
								<option value="HT">HT</option>
								<option value="WT">WT</option>
								<option value="BP">BP</option>
								<option value="BMI">BMI</option>
								<option value="WAIS">WAIS (waist)</option>
								<option value="WC">WC (waist circ.)</option>
								<option value="G">Gravida</option>
								<option value="P">Para</option>
								<option value="LMP">LMP</option>
								<option value="SMK">Smoking</option>
								<option value="HbAi">HbAi</option>
								<option value="A1C">A1C</option>
								<option value="FBS">FBS</option>
								<option value="TG">TG</option>
								<option value="LDL">LDL</option>
								<option value="HDL">HDL</option>
								<option value="TCHD">TCHD</option>
								<option value="TCHL">TCHL</option>
								<option value="EGFR">EGFR</option>
								<option value="SCR">SCR (Cr)</option>
								<option value="ACR">ACR</option>								</select>
							<br>
						<bean:message key="eFormGenerator.inputTypeMeasurementsCustom"/><input type="text" name="MeasurementCustom" id="MeasurementCustom" style="width:50px;">
						</p>
						</td>
						<td>
							<p><bean:message key="eFormGenerator.inputTypeMeasurementsField"/>
								<select name="MeasurementField" id="MeasurementField">
									<option value="value"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonValue"/></option>
									<option value="dateObserved"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonDateObserved"/></option>
									<option value="comments"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonComment"/></option>
								</select>
							</p>
						</td>
					</tr>
				</table>
			</li>
			<li id='SectionPrecheck' style="display:none"><input name="preCheck" id="preCheck" type="checkbox"><bean:message key="eFormGenerator.precheck"/></li>

		</ul>


	<span id="c_formating"><span class='h3'><bean:message key="eFormGenerator.inputFormat"/></span>
			<p>
			<bean:message key="eFormGenerator.inputFormatFont"/>
				<select id="fontFamily">
					 <option value="sans-serif"><bean:message key="eFormGenerator.inputFormatSelectSans"/></option>
					 <option value="serif"><bean:message key="eFormGenerator.inputFormatSelectSerif"/></option>
					 <option value="monospace"><bean:message key="eFormGenerator.inputFormatSelectMono"/></option>
				</select>
			<bean:message key="eFormGenerator.inputFormatStyle"/>
				<select id="fontStyle">
					 <option value="normal"><bean:message key="eFormGenerator.inputFormatStyleNormal"/></option>
					 <option value="italic"><bean:message key="eFormGenerator.inputFormatStyleItalic"/></option>
				</select>
			<span id="wtalign"><bean:message key="eFormGenerator.inputFormatWeight"/>
				<select id="fontWeight">
					 <option value="normal"><bean:message key="eFormGenerator.inputFormatStyleNormal"/></option>
					 <option value="bold"><bean:message key="eFormGenerator.inputFormatWeightBold"/></option>
					 <option value="bolder"><bean:message key="eFormGenerator.inputFormatWeightBolder"/></option>
					 <option value="lighter"><bean:message key="eFormGenerator.inputFormatWeightLighter"/></option>
				</select>
			<br></span>
			<bean:message key="eFormGenerator.inputFormatSize"/><input type="text" name="fontSize" id="fontSize"  style="width:50px" value="12"><bean:message key="eFormGenerator.inputFormatSizehint"/>
			<bean:message key="eFormGenerator.inputFormatAlign"/>
				<select id="textAlign">
					<option value="left"><bean:message key="eFormGenerator.inputFormatAlignLeft"/></option>
					<option value="center"><bean:message key="eFormGenerator.inputFormatAlignCenter"/></option>
					<option value="right"><bean:message key="eFormGenerator.inputFormatAlignRight"/></option>
					<option value="justify"><bean:message key="eFormGenerator.inputFormatAlignJustify"/></option>
				</select>
			</p>
			<p><bean:message key="eFormGenerator.inputFormatBackground"/>
				<select id="bgColor">
					<option value="transparent"><bean:message key="eFormGenerator.inputFormatBackgroundTransparent"/></option>
					<option value="white"><bean:message key="eFormGenerator.inputFormatBackgroundWhite"/></option>
				</select><br>
				- <bean:message key="eFormGenerator.inputFormatBackgroundhint"/>
			</p></span>



	<span class='h3'><bean:message key="eFormGenerator.inputName"/></span>
	    <p>
	    			<bean:message key="eFormGenerator.inputClass"/>
        				<select id="inputClass">
        					 <option value="" selected><bean:message key="eFormGenerator.inputClassNone"/></option>
        					 <option value="parent-field"><bean:message key="eFormGenerator.inputClassParent"/></option>
        					 <option value="child-"><bean:message key="eFormGenerator.inputClassChild"/></option>
        				</select>
        		    <bean:message key="eFormGenerator.inputParentclass"/><input type="text" name="inputParentclass" id="inputParentclass"  style="width:100px" value="">

        </p>
		<p><input type="radio" name="InputNameType" id="InputNameAuto" value="Auto" checked><bean:message key="eFormGenerator.inputNameSeq"/><br>
				- <bean:message key="eFormGenerator.inputNameSeqPrefix"/><input type="text" name="AutoNamePrefix" id="AutoNamePrefix" style="width:100px" value="AutoName"><br>
			<input type="radio" name="InputNameType" id="InputNameCustom" value="Custom"><bean:message key="eFormGenerator.inputNameSeqCustom"/>
				<input type="text" name="inputName" id="inputName">
				<br>
				- <bean:message key="eFormGenerator.inputNameSeqCustomhint1"/><br>
				- <bean:message key="eFormGenerator.inputNameSeqCustomhint2"/>
			<br>
			<span id="iiimeasures"><input type="radio" name="InputNameType" id="InputNameMeasurement" value="Measurement"><bean:message key="eFormGenerator.inputNameMeasurement"/><br>
			<table>
				<tr>
					<td><p><bean:message key="eFormGenerator.inputNameMeasurementType"/></p></td>
					<td><p>
						<select name="ExportMeasurementList" id="ExportMeasurementList">
							<option value="" selected="selected"><bean:message key="eFormGenerator.inputNameMeasurementButton"/></option>
								<option value="HT">HT</option>
								<option value="WT">WT</option>
								<option value="BP">BP</option>
								<option value="BMI">BMI</option>
								<option value="WAIS">WAIS (waist)</option>
								<option value="WC">WC (waist circ.)</option>
								<option value="G">Gravida</option>
								<option value="P">Para</option>
								<option value="LMP">LMP</option>
								<option value="SMK">Smoking</option>
								<option value="HbAi">HbAi</option>
								<option value="A1C">A1C</option>
								<option value="FBS">FBS</option>
								<option value="TG">TG</option>
								<option value="LDL">LDL</option>
								<option value="HDL">HDL</option>
								<option value="TCHD">TCHD</option>
								<option value="TCHL">TCHL</option>
								<option value="EGFR">EGFR</option>
								<option value="SCR">SCR (Cr)</option>
								<option value="ACR">ACR</option>	
						</select>
						<br>
				&nbsp;<bean:message key="eFormGenerator.inputNameMeasurementsCustom"/>&nbsp;<input type="text" name="ExportMeasurementCustom" id="ExportMeasurementCustom" style="width:50px;">
					</p>
					</td>
					<td>
						<p><bean:message key="eFormGenerator.inputNameMeasurementsField"/>
							<select name="ExportMeasurementField" id="ExportMeasurementField">
								<option value="value"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonValue"/></option>
								<option value="dateObserved"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonDateObserved"/></option>
								<option value="comments"><bean:message key="eFormGenerator.inputTypeMeasurementsFieldButtonComment"/></option>
							</select>
						</p>
					</td>
				</tr>
			</table>

		</p></span>
	<span class='h3'><bean:message key="eFormGenerator.inputDraw"/></span>
	<br>
		<span class='h4'><bean:message key="eFormGenerator.inputDrawText"/></span>
			<p>
			- <bean:message key="eFormGenerator.inputDrawTexthint"/><br>
			</p>
		<span class='h4'><bean:message key="eFormGenerator.inputDrawCheckbox"/></span>
			<p>
			- <bean:message key="eFormGenerator.inputDrawCheckboxhint"/><br>
			</p>
	<p><input type="button" onclick="Undo();" value='<bean:message key="eFormGenerator.inputDrawUndoButton"/>'></p>
	<p><bean:message key="eFormGenerator.inputDrawhint"/></p>

</div>

<hr>
<span class='h2'>6. <bean:message key="eFormGenerator.tuning"/></span><a onclick="show('Section6');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section6');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section6">

<input type="button" value='<bean:message key="eFormGenerator.tuningShowButton"/>' onclick="ToggleInputName();"><br>
<table style="text-align:center; border: 1px solid black;">
	<tr>
		<td style="background-color:#dddddd;">
			<input type="button" value='<bean:message key="eFormGenerator.tuningNoneButton"/>' onclick="uncheckList('InputChecklist');"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningAllButton"/>' onclick="checkList('InputChecklist');">
		</td>
		<td>
			<span><bean:message key="eFormGenerator.tuningUpButton"/></span><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningAlignButton"/>' style="width:100px;" onclick="alignInput('top');"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningShiftButton"/> [alt]↑' style="width:100px;" onclick="changeInput('up',10);"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningNudgeButton"/> ↑' style="width:100px;" onclick="changeInput('up',1);">
		</td>
		<td style="background-color:#dddddd;">
			<input type="button" value='<bean:message key="eFormGenerator.tuningDeleteButton"/>' Style="width:100px;" onclick="deleteInput();">
		</td>
	</tr>
	<tr>
		<td>
			<span><bean:message key="eFormGenerator.tuningLeft"/></span><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningAlignButton"/>' style="width:50px;" onclick="alignInput('left');">
			<input type="button" value='<bean:message key="eFormGenerator.tuningShiftButton"/>' style="width:50px;" onclick="changeInput('left',10);">
			<input type="button" value='<bean:message key="eFormGenerator.tuningNudgeButton"/>' style="width:50px;" onclick="changeInput('left',1);">
		</td>
		<td style="text-align:left;">
			<ul id="InputList" name="InputList" style="list-style-type:none; list-style: none; margin-left: 0; padding-left: 1em; text-indent: -1em"></ul>
		</td>
		<td>
			<span><bean:message key="eFormGenerator.tuningRight"/></span><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningNudgeButton"/>' style="width:50px;" onclick="changeInput('right',1);">
			<input type="button" value='<bean:message key="eFormGenerator.tuningShiftButton"/>' style="width:50px;" onclick="changeInput('right',10);">
			<input type="button" value='<bean:message key="eFormGenerator.tuningAlignButton"/>' style="width:50px;" onclick="alignInput('right');">
		</td>
	</tr>
	<tr>
		<td style="background-color:#dddddd;">
			<span><bean:message key="eFormGenerator.tuningWidth"/></span><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningIncreaseButton"/>  ⇑+→' style="width:120px;" onclick="changeInput('width',1);"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningDecreaseButton"/>  ⇑+←"' style="width:120px;" onclick="changeInput('width',-1);">
		</td>
		<td>

			<input type="button" value='<bean:message key="eFormGenerator.tuningNudgeButton"/> ↓' style="width:100px;" onclick="changeInput('down',1);"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningShiftButton"/> [alt]↓' style="width:100px;" onclick="changeInput('down',10);"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningAlignButton"/>' style="width:100px;" onclick="alignInput('bottom');"><br>
			<span><bean:message key="eFormGenerator.tuningDown"/></span>
			</td>
		<td style="background-color:#dddddd;">
			<span><bean:message key="eFormGenerator.tuningHeight"/></span><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningIncreaseButton"/> ⇑+↓' style="width:120px;" onclick="changeInput('height',1);"><br>
			<input type="button" value='<bean:message key="eFormGenerator.tuningDecreaseButton"/> ⇑+↑' style="width:120px;" onclick="changeInput('height',-1);">
		</td>
	</tr>
</table>

</div>
<hr>
<span class='h2'>7. <bean:message key="eFormGenerator.misc"/></span><a onclick="show('Section7');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section7');"><bean:message key="eFormGenerator.collapse"/></a>
<div id="Section7">
<p><span class="h2"><bean:message key="eFormGenerator.miscMax"/></span><br>
	<input name="maximizeWindow" id="maximizeWindow" type="checkbox"><bean:message key="eFormGenerator.miscMaxhint"/>
</p>
<p><span class='h2'><bean:message key="eFormGenerator.date"/></span><br>
	<input name="AddDate" id="AddDate" type="checkBox" checked><bean:message key="eFormGenerator.dateDescription"/>
</p>
	<p><span class='h2'><bean:message key="eFormGenerator.miscCheckmarks"/></span><br>
<input name="BlackBox" id="BlackBox" type="checkbox">
<bean:message key="eFormGenerator.BlackBox"/>
<br>

	<input name="ScaleCheckmark" id="ScaleCheckmark" type="checkbox"><bean:message key="eFormGenerator.miscCheckmarksScale"/><br>
	<input name="DefaultCheckmark" id="DefaultCheckmark" type="checkbox" style="display:none"><span style="display:none"><bean:message key="eFormGenerator.miscCheckmarksDraw"/></span>
</p>
<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_fax_enabled")) { %>
<p><span class='h2'><bean:message key="eFormGenerator.fax"/></span><br>
	<input name="includeFaxControl" id="includeFaxControl" type="checkBox"><bean:message key="eFormGenerator.faxDescription"/><br>
	<bean:message key="eFormGenerator.faxnumber"/>:
						<input type="text" name="faxno" id="faxno" style="width:200px;">
</p>
<% } %>

<% if (OscarProperties.getInstance().isPropertyActive("eform_generator_indivica_print_enabled")) { %>
<div id='pdfOption'>
<p><span class='h2'><bean:message key="eFormGenerator.PDFprint"/></span><br>
	<input name="includePdfPrintControl" id="includePdfPrintControl" type="checkBox">
<bean:message key="eFormGenerator.includePDFprint"/>
</p>
</div>
<% } %>

</div>
<hr>
<span class='h2'>8. <bean:message key="eFormGenerator.generate"/></span><a onclick="show('Section8');"><bean:message key="eFormGenerator.expand"/></a>/<a onclick="hide('Section8');"><bean:message key="eFormGenerator.collapse"/></a>
<div id='Section8'>
<!-- Inject the html to the eForm window -->
		<input name="loadHTMLButton" id="loadHTMLButton" type="button" value='<bean:message key="eFormGenerator.generateLoadButton"/>' onClick="injectHtml();">
	<input name="reset" id="reset" type="button" value='<bean:message key="eFormGenerator.generateResetButton"/>' onclick="resetAll();">
<!--  Cookie Monster says hello! -->
	<input name="save" id="save" type="button" value='<bean:message key="eFormGenerator.generateSaveButton"/>' onclick="save_to_cookie();">
	<input name="restore" id="restore" type="button" value='<bean:message key="eFormGenerator.generateRestoreSaveButton"/>' onclick="restoreSaved();">
<!--  Cookie Monster says bye! -->
	<p>- <bean:message key="eFormGenerator.generatehint1"/>
                <br>- <bean:message key="eFormGenerator.generatehint2"/>
		<br>- <bean:message key="eFormGenerator.generatehint3"/>
                <br>- <bean:message key="eFormGenerator.generatehint4"/>
	</p>

</div>

</div>
</form>

<!--  Drawing code: start -->
<div id="preview" name="preview" style="position: absolute; left: 0px; top: 0px;"></div>
<div id="myCanvas" name="myCanvas" style="position: absolute; left: 0px; top: 0px;"></div>

<script type="text/javascript">
var DrawData = new Array();
var TempData = new Array();

var cnv = document.getElementById("myCanvas"); 
var jg = new jsGraphics(cnv);

var pvcnv = document.getElementById("preview");
var pv = new jsGraphics(pvcnv);

jg.setPrintable(true);
var StrokeColor = "red";
var StrokeThickness = 2;
var x0 = 0;
var y0 = 0;
var ShowInputName = false;

function clearGraphics(canvas){
	canvas.clear();
}

function SetStrokeColor(c){
	StrokeColor = c;
}

var MouseDown = false;
function SetMouseDown(){
	MouseDown = true;
}
function SetMouseUp(){
	MouseDown = false;
}

var DrawSwitch = false;

function SetDrawOn(){
	DrawSwitch = true;
}
function SetDrawOff(){
	DrawSwitch  = false;
}

var TextSwitch = true;
var TextboxSwitch = false;
var CheckboxSwitch = false;
var XboxSwitch = false;
var MaleSwitch = false;
var FemaleSwitch = false;
var SignatureBoxSwitch = false;
var StampSwitch = false;
var ClassicSignatureSwitch = false;
var RadioButtonSwitch = false;

function SetSwitchesOff(){
	TextSwitch = false;
	TextboxSwitch = false;
	CheckboxSwitch = false;
	XboxSwitch = false;
	MaleSwitch = false;
	FemaleSwitch = false;
	SignatureBoxSwitch = false;
	StampSwitch = false;
	ClassicSignatureSwitch = false;
    RadioButtonSwitch = false;
}

var DrawTool = "Text";

function SetSwitchOn(n){	
	SetSwitchesOff();
	DrawTool = n;

	if	(n=="Text"){
		TextSwitch = true;
	}else if (n=="Textbox"){
		TextboxSwitch = true;
	}else if(n=="Checkbox"){
		CheckboxSwitch = true;
	}else if(n=="Xbox"){
		XboxSwitch = true;
	}else if (n=="Male"){
		MaleSwitch = true;
	}else if (n=="Female"){
		FemaleSwitch = true;
	}else if (n=="SignatureBox"){
		SignatureBoxSwitch = true;
	}else if (n=="Stamp"){
		StampSwitch = true;
	}else if (n=="ClassicSignature") {
		ClassicSignatureSwitch = true;
	}else if (n=="RadioButton") {
		RadioButtonSwitch = true;
	}
}
function SetStart(){
	x0 = parseInt(mousex);	//assign x coordinate at mousedown to x0
	y0 = parseInt(mousey);	//assign y coordinate at mousedown to y0
}

function DrawText(canvas,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass){
	// draw Rectangle
//alert(PageIterate+"|"+PageNum+"|"+( PageIterate == PageNum )+(canvas == jg));
	if ( PageIterate == PageNum ) {
		var x0 = parseInt(x0);
		var y0 = parseInt(y0);
		var width = parseInt(width);
		var height = parseInt(height);
		canvas.setColor(StrokeColor);
		canvas.setStroke(StrokeThickness);
		canvas.drawRect(x0,y0,width,height);
		canvas.paint();
		if (ShowInputName){
			canvas.setColor('blue');
			canvas.setFont("sans-serif","10px",Font.BOLD);
			var xt = x0 + StrokeThickness
			var yt = y0 + StrokeThickness
			canvas.drawString(inputName,xt,y0);
			canvas.paint();
			canvas.setColor(StrokeColor);
		}
	}
	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Text" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName + "|" + fontFamily + "|" + fontStyle + "|" + fontWeight + "|" + fontSize + "|" + textAlign + "|" + bgColor + "|" + oscarDB + "|" + inputValue+ "|" + inputClass + "|" + inputParentclass;
		DrawData.push(Parameter);
	}
}

function DrawTextbox(canvas,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass){
	// draws Rectangle
	if ( PageIterate == PageNum ) {
		var x0 = parseInt(x0);
		var y0 = parseInt(y0);
		var width = parseInt(width);
		var height = parseInt(height);
		canvas.setColor(StrokeColor);
		canvas.setStroke(StrokeThickness);
		canvas.drawRect(x0,y0,width,height);
		canvas.paint()
		if (ShowInputName){
			canvas.setColor('blue');
			canvas.setFont("sans-serif","10px",Font.BOLD);
			var xt = x0 + StrokeThickness
			var yt = y0 + StrokeThickness
			canvas.drawString(inputName,xt,y0);
			canvas.paint();
			canvas.setColor(StrokeColor);
		}
	}
	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Textbox" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName + "|" + fontFamily + "|" + fontStyle + "|" + fontWeight + "|" + fontSize + "|" + textAlign + "|" + bgColor + "|" + oscarDB + "|" + inputValue+ "|" + inputClass + "|" + inputParentclass;
		DrawData.push(Parameter);
	}
}

function DrawCheckbox(canvas,x0,y0,inputName,preCheck,inputClass,inputParentclass){
	// draws Checkbox
	if ( PageIterate == PageNum ) {
		var x = parseInt(x);
		var y = parseInt(y);
		canvas.setColor(StrokeColor);
		canvas.setStroke(StrokeThickness);
		var s = 10; 	//square with side of 10
		canvas.drawRect(x0,y0,s,s);
		canvas.paint();
		if (ShowInputName){
			canvas.setColor('blue');
			canvas.setFont("sans-serif","10px",Font.BOLD);
			var xt = x0 + StrokeThickness
			var yt = y0 + StrokeThickness
			canvas.drawString(inputName,xt,y0);
			canvas.paint();
			canvas.setColor(StrokeColor);
		}
		canvas.paint();
	}
	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Checkbox" + "|" + x0 + "|" + y0 + "|" + inputName + "|" + preCheck + "|" + inputClass + "|" + inputParentclass;
		DrawData.push(Parameter);
	}
	if ((inputName == "Male")||(inputName == "Female")){ 
		SetSwitchOn('Text');
		document.getElementById('Text').click();
	}
}

function DrawXbox(canvas,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue){
	// draw Rectangle
	if ( PageIterate == PageNum ) {
		var x0 = parseInt(x0);
		var y0 = parseInt(y0);
		var s = 10; 	//square with side of 10
		var s = parseInt(fontSize)+2;
		canvas.setColor(StrokeColor);
		canvas.setStroke(StrokeThickness);
		canvas.drawRect(x0,y0,s,s);
		canvas.paint();
		if (ShowInputName){
			canvas.setColor('blue');
			canvas.setFont("sans-serif","10px",Font.BOLD);
			var xt = x0 + StrokeThickness
			var yt = y0 + StrokeThickness
			canvas.drawString(inputName,xt,y0);
			canvas.paint();
			canvas.setColor(StrokeColor);
		}
	}
	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Xbox" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName + "|" + fontFamily + "|" + fontStyle + "|" + fontWeight + "|" + fontSize + "|" + textAlign + "|" + bgColor + "|" + oscarDB + "|" + inputValue+ "|" + inputClass + "|" + inputParentclass;
		DrawData.push(Parameter);
	}
	if ((inputName == "Male")||(inputName == "Female")){ 
		SetSwitchOn('Text');
		document.getElementById('Text').click();
	}

}

function DrawPage(canvas,pnum,pimage,bwidth){
//alert("page draw"+pnum)
	PageIterate = pnum;
	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Page" + "|" + pnum + "|" + pimage + "|" + bwidth;
		DrawData.push(Parameter);
	}
//var tempa="";
//for (j=0; (j < (DrawData.length) ); j++){
//		tempa += DrawData[j]+"\n";
//	}
//alert(tempa);
}

function DrawSignatureBox(canvas,x0,y0, width, height, inputName){

	if (inputName == "ClassicSignature"){
		//assigns coordinates of top left corner of box
		SignatureHolderX = x0;
		SignatureHolderY = y0;
		SignatureHolderW = width;
		SignatureHolderH = height;
		sigOffset = pageoffset - document.getElementById('BGImage').height;

	} else {
		//constrains width and height
		if (height<30) { height=30;}
		width=4*height;
	}

	//draws box
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	canvas.drawRect(x0,y0,width,height);
	canvas.paint();
	
	if(ShowInputName){
		canvas.setColor('blue');
		canvas.setFont("sans-serif","10px",Font.BOLD);
		var xt = x0 + StrokeThickness
		var yt = y0 + StrokeThickness
		canvas.drawString(inputName,xt,y0);
		canvas.paint();
		canvas.setColor(StrokeColor);
	}

	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Signature" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName;
		DrawData.push(Parameter);
	}

	//reset to default input of text input
	SetSwitchOn('Text');
	document.getElementById('Text').click();
}

function DrawStamp(canvas,x0,y0, width, height, inputName){
	//draws box
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	canvas.drawRect(x0,y0,width,height);
	canvas.paint();
	
	if(ShowInputName){
		canvas.setColor('blue');
		canvas.setFont("sans-serif","10px",Font.BOLD);
		var xt = x0 + StrokeThickness
		var yt = y0 + StrokeThickness
		canvas.drawString(inputName,xt,y0);
		canvas.paint();
		canvas.setColor(StrokeColor);
	}

	//store parameters in an array (using separator "|")
	if (canvas == jg){ 
		var Parameter = "Stamp" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName;
		DrawData.push(Parameter);
	}

	//reset to default input of text input
	SetSwitchOn('Text');
	document.getElementById('Text').click();
}

var inputName="";
var inputCounter = 1;

	
function DrawMarker(){
	var x = parseInt(mousex);	//assign x coordinate at mouseup to x
	var y = parseInt(mousey);	//assign y coordinate at mouseup to y

		
	var width = x - x0;	
	var height = y - y0;
	var fontFamily = document.getElementById('fontFamily').value;
	var fontStyle = document.getElementById('fontStyle').value;
	var fontWeight = document.getElementById('fontWeight').value;
	var fontSize = document.getElementById('fontSize').value;
	var inputClass = document.getElementById('inputClass').value;
	var inputParentclass = document.getElementById('inputParentclass').value;
	var textAlign = document.getElementById('textAlign').value;
	var bgColor = document.getElementById('bgColor').value;

	//get value of autopopulating data
	var preCheck = document.getElementById('preCheck').checked
	var inputValue = "";
	var oscarDB = "";
	var AutoPopType = getCheckedValue(document.getElementsByName('AutoPopType'));
	if (AutoPopType == 'custom'){
		inputValue = document.getElementById('inputValue').value;
	}else if (AutoPopType == 'database'){
		oscarDB = document.getElementById('oscarDB').value;
	}else if (AutoPopType == 'measurements'){
		if (document.getElementById('MeasurementList').value){	// Common Standard MeasurementTypes
			oscarDB = "m$" + document.getElementById('MeasurementList').value + "#" + document.getElementById('MeasurementField').value;
		}else if (document.getElementById('MeasurementCustom').value){	//Custom Measurement Types
			oscarDB = "m$" + document.getElementById('MeasurementCustom').value + "#" + document.getElementById('MeasurementField').value;
		}
	}

	//get name of input field
	var inputNameType = getCheckedValue(document.getElementsByName('InputNameType'));  // inputNameType = Auto/Custom
	if (inputNameType == "Custom"){
		e = document.getElementById('inputName').value
		if (e){
			inputName = e
		} else if (!e){
			alert("<bean:message key="eFormGenerator.emptyInput"/>");	//reminds user to put in mandatory name for input field
			return false;
		}
	} else if(inputNameType == "Measurement"){
		if (document.getElementById('ExportMeasurementList').value){
			inputName = "m$" + document.getElementById('ExportMeasurementList').value + "#" + document.getElementById('ExportMeasurementField').value;
		}else if (document.getElementById('ExportMeasurementCustom').value){
			inputName = "m$" + document.getElementById('ExportMeasurementCustom').value + "#" + document.getElementById('ExportMeasurementField').value;
		}		
	}else if (inputNameType == "Auto") {
		if (oscarDB){
			inputName = oscarDB;	//if auto-naming input fields, use oscarDB tag if available
			var InputList = document.getElementsByName('InputChecklist');
			var j = 0;
			for (i=0; i < InputList.length; i++){
				var InputItem = InputList[i].value.substring(0,inputName.length);	//add increment to oscarDB name if repeated
				if (InputItem == inputName){
				 j++;
				}
			}
			if (j>0){
				inputName = inputName + j;	
			}
		}else{
			inputName = document.getElementById('AutoNamePrefix').value + inputCounter;
			++inputCounter;
		}
	}
	//compare inputName to list of existing inputNames to ensure unique names
	for (i=0; i < document.getElementsByName('InputChecklist').length; i++){
		var InputItem = document.getElementsByName('InputChecklist')[i].value;
		if (inputName == InputItem){
			alert("<bean:message key="eFormGenerator.duplicateName"/>");
		}
	}
	
	
	if(DrawSwitch){
		if (TextSwitch){
			DrawText(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
		}else if (TextboxSwitch){
			DrawTextbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
		}else if (CheckboxSwitch){
			DrawCheckbox(jg,x0,y0,inputName,preCheck,inputClass,inputParentclass);
		}else if (XboxSwitch){
			if (preCheck){ inputValue='X'; } else {inputValue='';}
			DrawXbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
		}else if(MaleSwitch){
			if (document.getElementById('XboxType').checked) {
				DrawXbox(jg,x0,y0,width,height,"Male",fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,"","","");
			} else {
				DrawCheckbox(jg,x0,y0,"Male",false,"only-one-","gender");
			}
		}else if(FemaleSwitch){
			if (document.getElementById('XboxType').checked) {
				DrawXbox(jg,x0,y0,width,height,"Female",fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,"","","");
			} else {
				DrawCheckbox(jg,x0,y0,"Female",false,"only-one-","gender");
			}
		}else if (SignatureBoxSwitch){
			var sigtext ="Signature";
			sigint += 1;
			sigtext = sigtext + sigint
			DrawSignatureBox(jg,x0,y0,width,height,sigtext);
		}else if (StampSwitch){
			var sigtext ="Stamp";
			DrawStamp(jg,x0,y0,width,height,sigtext);
		}else if (ClassicSignatureSwitch){
			var sigtext ="ClassicSignature";
			DrawSignatureBox(jg,x0,y0,width,height,sigtext);
		}else if (RadioButtonSwitch){
			DrawCheckbox(jg,x0,y0,inputName,false,"only-one-",document.getElementById('RadioName').value);
		} else {
            alert("nothing selected!");
        }	
	}
	
	//reset input data
	document.getElementById('inputValue').value = "";
	document.getElementById('inputName').value = "";
	//document.getElementById('bgColor')[0].selected = true;
	document.getElementById('preCheck').checked = false;
	document.getElementById('oscarDB')[0].selected = true;
	document.getElementById('MeasurementList')[0].selected = true;
	document.getElementById('ExportMeasurementList')[0].selected = true;
	document.getElementById('MeasurementCustom').value = "";
	document.getElementById('ExportMeasurementCustom').value = "";
	document.getElementById('inputClass')[0].selected = true;
	document.getElementById('inputParentclass').value = "";	
}

function ToggleInputName(){
	PageIterate=0;
	jg.clear();
	if (ShowInputName){
		ShowInputName = false;
	} else if (!ShowInputName){
		ShowInputName = true;
	}
	drawPageOutline();
	TempData = DrawData;
	DrawData = new Array();
	for (j=0; (j < (TempData.length) ); j++){
		var RedrawParameter = TempData[j].split("|");
		RedrawImage(RedrawParameter);
	}
}

function RedrawAll(){
	PageIterate=0;
	jg.clear();

	drawPageOutline();
	TempData = DrawData;
	DrawData = new Array();
	for (j=0; (j < (TempData.length) ); j++){
		var RedrawParameter = TempData[j].split("|");
		RedrawImage(RedrawParameter);
	}
}

function Undo(){
	jg.clear();
	TempData = DrawData;
	DrawData = new Array();
	
	drawPageOutline();
	for (j=0; (j < (TempData.length - 1) ); j++){
		var RedrawParameter = TempData[j].split("|");
		RedrawImage(RedrawParameter);
	}
	var inputNameType = getCheckedValue(document.getElementsByName('InputNameType'));  // inputNameType = Auto/Custom
	if (inputNameType == "Auto") {
		--inputCounter;
	}
	loadInputList();	
}

function RedrawImage(RedrawParameter){
	var InputType = RedrawParameter[0];
	if(InputType == "Text"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var width = parseInt(RedrawParameter[3]);	
		var height = parseInt(RedrawParameter[4]);	
		var inputName = RedrawParameter[5];	
		var fontFamily = RedrawParameter[6];
		var fontStyle = RedrawParameter[7];
		var fontWeight = RedrawParameter[8];
		var fontSize = RedrawParameter[9];
		var textAlign = RedrawParameter[10];
		var bgColor = RedrawParameter[11];
		var oscarDB = RedrawParameter[12];
		var inputValue = RedrawParameter[13];
		var inputClass = RedrawParameter[14];
		var inputParentclass = RedrawParameter[15];
		DrawText(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
	}else if (InputType == "Textbox"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var width = parseInt(RedrawParameter[3]);	
		var height = parseInt(RedrawParameter[4]);	
		var inputName = RedrawParameter[5];	
		var fontFamily = RedrawParameter[6];
		var fontStyle = RedrawParameter[7];
		var fontWeight = RedrawParameter[8];
		var fontSize = RedrawParameter[9];
		var textAlign = RedrawParameter[10];
		var bgColor = RedrawParameter[11];
		var oscarDB = RedrawParameter[12];
		var inputValue = RedrawParameter[13];
		var inputClass = RedrawParameter[14];
		var inputParentclass = RedrawParameter[15];
		DrawTextbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
	}else if (InputType == "Checkbox"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var inputName = RedrawParameter[3];
		var preCheck = RedrawParameter[4];
		var inputClass = RedrawParameter[5];
		var inputParentclass = RedrawParameter[6];
		DrawCheckbox(jg,x0,y0,inputName,preCheck,inputClass,inputParentclass);
	}else if (InputType == "Xbox"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var width = parseInt(RedrawParameter[3]);	
		var height = parseInt(RedrawParameter[4]);	
		var inputName = RedrawParameter[5];	
		var fontFamily = RedrawParameter[6];
		var fontStyle = RedrawParameter[7];
		var fontWeight = RedrawParameter[8];
		var fontSize = RedrawParameter[9];
		var textAlign = RedrawParameter[10];
		var bgColor = RedrawParameter[11];
		var oscarDB = RedrawParameter[12];
		var inputValue = RedrawParameter[13];
		var inputClass = RedrawParameter[14];
		var inputParentclass = RedrawParameter[15];
		DrawXbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue,inputClass,inputParentclass);
	}else if (InputType == "Page"){
		var pnum = parseInt(RedrawParameter[1]);
		PageIterate = pnum;
		var pimage = RedrawParameter[2];
		var bwidth = parseInt(RedrawParameter[3]);
		DrawPage(jg,pnum,pimage,bwidth);
	}else if (InputType == "Signature"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var width = parseInt(RedrawParameter[3]);	
		var height = parseInt(RedrawParameter[4]);
		var inputName = RedrawParameter[5];
		DrawSignatureBox(jg,x0,y0,width,height,inputName);
	}else if (InputType == "Stamp"){
		var x0 = parseInt(RedrawParameter[1]);
		var y0 = parseInt(RedrawParameter[2]);
		var width = parseInt(RedrawParameter[3]);	
		var height = parseInt(RedrawParameter[4]);
		var inputName = RedrawParameter[5];
		DrawStamp(jg,x0,y0,width,height,inputName);
	}
}

function drawPortraitOutline(){
	jg.setColor('red');
	jg.setStroke(StrokeThickness);
	jg.drawRect(0,0,750,1000);
	jg.paint();
}
function drawLandscapeOutline(){
	jg.setColor('red');
	jg.setStroke(StrokeThickness);
	jg.drawRect(0,0,1000,750);
	jg.paint();
}
<!-- Drawing code ends -->
</script>
<!--  Cookie Monster says hello! -->
<script type="text/javascript">
function getCookie (name) {
    var dc = document.cookie;
    var cname = name + "=";

    if (dc.length > 0) {
      begin = dc.indexOf(cname);
      if (begin != -1) {
        begin += cname.length;
        end = dc.indexOf(";" ,begin);
        if (end == -1) end = dc.length;
        return dc.substring(begin, end);
        }
      }
    return null;
}
function save_to_cookie(){
var exp = new Date();
exp.setTime(exp.getTime() + (1000 * 60 * 60 * 24 * 30));
document.cookie = "drawdata" + "=" + DrawData + "; expires=" + exp.toGMTString() + "; path=/";
document.cookie = "inputcounter" + "=" + inputCounter + "; expires=" + exp.toGMTString() + "; path=/";
}
function restoreSaved(){
DrawData=getCookie("drawdata").split(",");
inputCounter=getCookie("inputcounter");
RedrawAll();
}
</script>
<!--  Cookie Monster says bye! -->
</body>
</html>
