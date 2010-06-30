<%@ page import="oscar.eform.actions.DisplayImageAction,java.lang.*,java.io.File,oscar.OscarProperties,java.io.*,oscar.eform.*,oscar.eform.data.*,java.util.*"%>


<!--
/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  modified by amos raphenya
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 *  eformGenerator.jsp
 *  
 *  Created on June 24, 2010, 3:45 PM
 *
 */
-->
<html>
<head>

<title>OSCAR e-Form Generator</title>

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
h1	{font-family: sans-serif; font-size: 14px; font-weight: bolder;}
h2	{font-family: sans-serif; font-size: 12px; font-weight: bold; text-decoration: underline;}
h3	{font-family: sans-serif; font-size: 12px; font-weight: bold;}
h4	{font-family: sans-serif; font-size: 12px; font-weight: normal; text-decoration: underline;}
p, li	{font-family: sans-serif; font-size: 12px; font-weight: normal;}
</style>

<script type="text/javascript">
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

var BGWidth = 0;
var BGHeight = 0;

function loadImage(){
var img = document.getElementById('imageName' );
var myCnv = document.getElementById('myCanvas');
var bg = document.getElementById('BGImage');

//set the path for image function
bg.src = ("<%=request.getContextPath()%>"+"/eform/displayImage.do?imagefile="+img.value);

	document.getElementById('OrientCustom').value = document.getElementById('OrientCustomValue').value;
	BGWidth = parseInt(getCheckedValue(document.getElementsByName('Orientation')));
	bg.width = BGWidth;
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

var text = "";
var textTop = "";
var textMiddle = "";
var textBottom = "";

var CheckboxOffset = 4;
var MTopLeftX = 0;
var MTopLeftY = 0;
var FTopLeftX = 0;
var FTopLeftY = 0;

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
	document.getElementById('Text').checked = true;

	document.getElementById('inputValue').value = "";
	document.getElementById('inputName').value = "";
	document.getElementById('preCheck').checked = false;
	document.getElementById('preCheckGender').checked = false;
	document.getElementById('maximizeWindow').checked = false;
	var l = document.getElementById('oscarDB');
		l[0].selected = true;
	document.getElementById('DefaultCheckmark').checked = false;
	clearGraphics(jg);
	loadImage();
}




function GetTextTop(){
	textTop = "&lt;html&gt;\n&lt;head&gt;\n&lt;title&gt;"
	textTop += document.getElementById('eFormName').value;
	textTop += "&lt;/title&gt;\n&lt;style type=&quot;text/css&quot; media=&quot;print&quot;&gt;\n .DoNotPrint {\n\t display: none;\n }\n .noborder {\n\t border : 0px;\n\t background: transparent;\n\t scrollbar-3dlight-color: transparent;\n\t scrollbar-3dlight-color: transparent;\n\t scrollbar-arrow-color: transparent;\n\t scrollbar-base-color: transparent;\n\t scrollbar-darkshadow-color: transparent;\n\t scrollbar-face-color: transparent;\n\t scrollbar-highlight-color: transparent;\n\t scrollbar-shadow-color: transparent;\n\t scrollbar-track-color: transparent;\n\t background: transparent;\n\t overflow: hidden;\n }\n &lt;/style&gt;\n\n"
	if (document.getElementById('preCheckGender').checked){
		textTop += "&lt;script type=&quot;text/javascript&quot; language=&quot;javascript&quot;&gt;\n"
		textTop += "function checkGender(){\n"
		textTop += "\t if (document.getElementById('PatientGender').value == 'M'){\n"
		textTop += "\t document.getElementById('Male').checked = true;\n"
		textTop += "\t }else if (document.getElementById('PatientGender').value == 'F'){\n"
		textTop += "\t document.getElementById('Female').checked = true;\n"
		textTop += "\t}\n }\n"
		textTop += "&lt;/script&gt;\n\n"
	}

	textTop += "&lt;!-- js graphics scripts --&gt;\n"
	textTop += "&lt;script type=&quot;text/javascript&quot; src=&quot;${oscar_image_path}jsgraphics.js&quot;&gt;&lt;/script&gt;\n"
	textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
	textTop += "function formPrint(){\n"
	textTop += "\t if (document.getElementById('DrawCheckmark').checked){ \n"
	textTop += "\t\t	printCheckboxes();\n"
	textTop += "\t }else{\n"
	textTop += "\t\t	window.print();\n"
	textTop += "\t } \n"
	textTop += "} \n"
	textTop += "&lt;/script&gt;\n\n"


	textTop += "&lt;!-- scripts to confirm closing of window if haven't saved yet --&gt;\n"
	textTop += "&lt;script language=&quot;javascript&quot;&gt;\n"
	textTop += "//keypress events trigger dirty flag\n"
	textTop += "var needToConfirm = true;\n"
	textTop += "document.onkeyup=setDirtyFlag;\n"
	textTop += "function setDirtyFlag(){\n"
	textTop += "\t	needToConfirm = true;\n"
	textTop += "}\n"
	textTop += "function releaseDirtyFlag(){\n"
	textTop += "\t	needToConfirm = false; //Call this function if doesn't requires an alert.\n"
	textTop += "//this could be called when save button is clicked\n"
	textTop += "}\n"
	textTop += "window.onbeforeunload = confirmExit;\n"
	textTop += "function confirmExit(){\n"
	textTop += "\t if (needToConfirm){\n"
	textTop += "\t\t return &quot;You have attempted to leave this page. If you have made any changes to the fields without clicking the Save button, your changes will be lost. Are you sure you want to exit this page?&quot;;\n"
	textTop += "\t }\n"
	textTop += "}\n"
	textTop += "&lt;/script&gt;\n\n"


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
	textTop += "&lt;/head&gt;\n\n"
	textTop += "&lt;body"
	if (document.getElementById('preCheckGender').checked){
		textTop += " onload=&quot;checkGender();&quot;"
	}
	textTop += "&gt;\n"

	textTop += "&lt;div  &gt;\n\t &lt;img src=&quot;${oscar_image_path}";

	textTop += document.getElementById('imageName').value;
	textTop += "&quot; style=&quot;position: absolute; left: 0px; top: 0px; width:"
	textTop += BGWidth;
	textTop += "&quot;&gt;\n\n"

	textTop += "&lt;div id=&quot;myCanvas&quot; style=&quot;position:absolute; left:0px; top:0px; width:"
	textTop += BGWidth
	textTop += "; height:"
	textTop += BGHeight
	textTop += ";&quot; onmouseover=&quot;putInBack();&quot;&gt;&lt;/div&gt;\n\n"
	textTop +=" &lt;form method=&quot;post&quot; action=&quot;efmformmanageredit.jsp&quot; name=&quot;FormName&quot; id=&quot;FormName&quot; &gt;\n";


}


function GetTextMiddle(P){
var InputType = P[0];
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

		m = "&lt;input name=&quot;"
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; type=&quot;text&quot; class=&quot;noborder&quot; style=&quot;position:absolute; left:"
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
		m = "&lt;textarea name=&quot;"
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; class=&quot;noborder&quot; style=&quot;position:absolute; left:"
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

		m = "&lt;input name=&quot;"
		m += inputName
		m += "&quot; id=&quot;"
		m += inputName
		m += "&quot; type=&quot;checkbox&quot; style=&quot;position:absolute; left:"
		var a = parseInt(x - CheckboxOffset);
		m += a
		m += "px; top:"
		var b = parseInt(y - CheckboxOffset);
		m += b
		m += "px; &quot;"
		if (preCheck == 'true'){
			m += " checked"
		}
		m += "&gt;\n"
	}

	textMiddle += m;
}

function GetTextBottom(){
	if (document.getElementById('preCheckGender').checked){
		textBottom += "&lt;input name=&quot;PatientGender&quot; id=&quot;PatientGender&quot; type=&quot;hidden&quot; oscarDB=sex&gt;\n"
		textBottom += "&lt;input name=&quot;Male&quot; id=&quot;Male&quot; type=&quot;checkbox&quot; class=&quot;noborder&quot; style=&quot;position:absolute; left: "
		textBottom += parseInt(MTopLeftX - CheckboxOffset);
		textBottom += "px; top: "
		textBottom += parseInt(MTopLeftY - CheckboxOffset);
		textBottom += "px&quot;&gt;\n"
		textBottom += "&lt;input name=&quot;Female&quot; id=&quot;Female&quot; type=&quot;checkbox&quot; class=&quot;noborder&quot; style=&quot;position:absolute; left: "
		textBottom += parseInt(FTopLeftX - CheckboxOffset);
		textBottom += "px; top: "
		textBottom += parseInt(FTopLeftY - CheckboxOffset);
		textBottom += "px&quot;&gt;\n"
	}

	textBottom += "\n\n &lt;div class=&quot;DoNotPrint&quot; style=&quot;position: absolute; top:"
	textBottom += document.getElementById('BGImage').height;
	textBottom += "px; left:0px;&quot;&gt;\n"
	textBottom += "\t &lt;table&gt;&lt;tr&gt;&lt;td&gt;\n"
	textBottom += "\t\t Subject: &lt;input name=&quot;subject&quot; size=&quot;40&quot; type=&quot;text&quot;&gt;\n"
	textBottom += "\t\t	&lt;input value=&quot;Submit&quot; name=&quot;SubmitButton&quot; id=&quot;SubmitButton&quot; type=&quot;submit&quot; onclick=&quot;releaseDirtyFlag();setTimout('document.FormName.submit()',1000);&quot;&gt; \n"
        textBottom += "\t\t     &lt;input value=&quot;Reset&quot; name=&quot;ResetButton&quot; id=&quot;ResetButton&quot; type=&quot;button&quot; onclick=&quot;document.FormName.reset();&quot;&gt; \n"
	textBottom += "\t\t	&lt;input value=&quot;Print&quot; name=&quot;PrintButton&quot; id=&quot;PrintButton&quot; type=&quot;button&quot; onclick=&quot;formPrint();&quot;&gt; \n"
	textBottom += "\t\t	&lt;input value=&quot;Print &amp; Submit&quot; name=&quot;PrintSubmitButton&quot; id=&quot;PrintSubmitButton&quot; type=&quot;button&quot; onclick=&quot;formPrint();releaseDirtyFlag();setTimeout('document.FormName.submit()',1000);&quot;&gt; \n"
	textBottom += "\t\t	&lt;input name=&quot;DrawCheckmark&quot; id=&quot;DrawCheckmark&quot; type=&quot;checkbox&quot;"
	if(document.getElementById('DefaultCheckmark').checked){
		textBottom += ' checked'
	}
	textBottom += "&gt;"
	textBottom += "&lt;span style=&quot;font-family:sans-serif; font-size:12px;&quot;&gt;Draw Checkmarks&lt;/span&gt; \n"
	textBottom += "\t &lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;\n"
	textBottom += " &lt;/div&gt;\n &lt;/form&gt;\n\n"



	textBottom += "&lt;script type=&quot;text/javascript&quot;&gt;\n"
	textBottom += "var cnv = document.getElementById(&quot;myCanvas&quot;); \n"
	textBottom += "var jg = new jsGraphics(cnv);\n"
	textBottom += "var cnvLeft = parseInt(cnv.style.left); 	\n"
	textBottom += "var cnvTop = parseInt(cnv.style.top);\n"
	textBottom += "jg.setPrintable(true);\n"
	textBottom += "var StrokeColor = &quot;black&quot;;\n"
	textBottom += "var StrokeThickness = 3;\n"
	textBottom += "var x0 = 0;\n"
	textBottom += "var y0 = 0;\n"
	textBottom += "function drawCheckmark(x,y){\n"
	textBottom += "var offset = 6;\n"
	textBottom += "var x = parseInt(x) + offset;\n"
	textBottom += "var y = parseInt(y) + offset;\n"
	textBottom += "jg.setColor(StrokeColor);\n"
	textBottom += "jg.setStroke(StrokeThickness);\n"
	textBottom += "\t	// draws checkmark\n"
	textBottom += "\t	var x1 = x;\n"
	textBottom += "\t	var y1 = y+4;\n"
	textBottom += "\t	var x2 = x1 + 3;\n"
	textBottom += "\t	var y2 = y1 + 4;\n"
	textBottom += "\t	var x3 = x2 + 4;\n"
	textBottom += "\t	var y3 = y2 - 12;\n"
	textBottom += "\t	jg.drawLine(x1,y1,x2,y2);\n"
	textBottom += "\t	jg.drawLine(x2,y2,x3,y3);\n"
	textBottom += "\t	jg.paint();\n"
	textBottom += "}\n"
	textBottom += "function replaceCheckmarks(){\n"
	textBottom += "var f = document.getElementById(&quot;FormName&quot;);\n"
	textBottom += "	\t for (var i=0;i&lt;f.length;i++){\n"
	textBottom += "	\t\t	if ((f.elements[i].type == 'checkbox') &amp;&amp; (f.elements[i].checked)){\n"
	textBottom += "	\t\t		var a = f.elements[i].style.left;\n"
	textBottom += "	\t\t		var b = f.elements[i].style.top;\n"
	textBottom += "	\t\t		drawCheckmark(a,b);\n"
	textBottom += "	\t\t	}\n"
	textBottom += "	\t }\n"
	textBottom += "}\n"
	textBottom += "function printCheckboxes(){\n"
	textBottom += "\t	putInFront();\n"
	textBottom += "\t	replaceCheckmarks();\n"
	textBottom += "\t	window.print();\n"
	textBottom += "}\n"
	textBottom += "function putInFront(){\n"
	textBottom += "\t	cnv.style.zIndex = &quot;999999&quot;;	\n"
	textBottom += "}\n"
	textBottom += "function putInBack(){\n"
	textBottom += "\t	cnv.style.zIndex = &quot;-999999&quot;;	\n"
	textBottom += "}\n"
	textBottom += "&lt;/script&gt;\n"


	textBottom += "&lt;/body&gt;\n &lt;/html&gt;\n";
}

function popUp(){

textTop = "";
GetTextTop();

textMiddle = "";
var m = ""
for (j=0; (j < (DrawData.length) ); j++){
		var GetTextMiddleParameter = DrawData[j].split("|");
		GetTextMiddle(GetTextMiddleParameter);
	}

textBottom = "";
GetTextBottom();

//saves all generated html into variable 'text''
text = textTop  + textMiddle + textBottom;

return text;

}

//this function used for injecting html in to Edit E-Form in efmformmanageredit.jsp w/ variable formHtml
function injectHtml(){
    document.getElementById('formHtml').value = popUp();
    document.getElementById('toSave').submit();
}
//this function used for injecting html in to Edit E-Form in efmformmanageredit.jsp w/ variable formHtml BUT not efficient
function injectHtml2(){
window.open('efmformmanageredit.jsp?formHtml=','happyWindow');
happyWindow.document.getElementById('formHtml').value = popUp();
}


</script>

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
<script type="text/javascript">
/* This notice must be untouched at all times.

wz_jsgraphics.js    v. 3.01
The latest version is available at
http://www.walterzorn.com
or http://www.devira.com
or http://www.walterzorn.de

Copyright (c) 2002-2004 Walter Zorn. All rights reserved.
Created 3. 11. 2002 by Walter Zorn (Web: http://www.walterzorn.com )
Last modified: 1. 6. 2007

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


function _chkDHTM(x, i)
{
	x = document.body || null;
	jg_ie = x && typeof x.insertAdjacentHTML != "undefined" && document.createElement;
	jg_dom = (x && !jg_ie &&
		typeof x.appendChild != "undefined" &&
		typeof document.createRange != "undefined" &&
		typeof (i = document.createRange()).setStartBefore != "undefined" &&
		typeof i.createContextualFragment != "undefined");
	jg_fast = jg_ie && document.all && !window.opera;
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
		'$1;left:$2;top:$3;width:$4;height:$5"></div>');
}

function _htmPrtRpc()
{
	return this.htm.replace(
		_regex,
		'<div style="overflow:hidden;position:absolute;background-color:'+
		'$1;left:$2;top:$3;width:$4;height:$5;border-left:$4px solid $1"></div>');
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
			'width:' +  w + 'px;'+
			'height:' + h + 'px;">'+
			'<img src="' + imgSrc + '" width="' + w + '" height="' + h + '"' + (a? (' '+a) : '') + '>'+
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

	if(!jg_ok) _chkDHTM();
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
			this.cnv = document.createElement("div");
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

</head>


<body onload="init();">

<img name="BGImage" id="BGImage" style="position: absolute; left: 0px; top: 0px;"
	onmouseover="SetDrawOn();"
	onmouseout="SetDrawOff();"
	onmousedown="if (event.preventDefault) event.preventDefault(); SetMouseDown();SetStart();"
	onmousemove=""
	onmouseup="SetMouseUp(); DrawMarker();">


<h1>E-form Generator</h1>

<!-- this form  used for injecting html in to Edit E-Form-->
<form method="post" action="efmformmanageredit.jsp" id="toSave">
    <input type="hidden" name="formHtml" id="formHtml" />
</form>

<form method="post" action="" name="FormName" id="FormName">
<div name="Wizard" id="Wizard" class="DoNotPrint" style="position: absolute; leftoscar_image_path:750px; top: 0px; width: 500px;" >

<hr>
<h2>1. Load Image:</h2>
       <script type="text/javascript">
        </script>
        <p><select name="imageName" id="imageName">
                 <option value=""                    >choose an image...</option>
                    <%
                    /**
                        this function/scriplet look in images directory and populate the selection
                        so that the user can select which image they want to use for generating an eform
                    */
                     OscarProperties oscarProps = OscarProperties.getInstance();

                     DisplayImageAction test = new DisplayImageAction();
                     File dir=null;
                        try {
                            dir =new File(oscarProps.getProperty("eform_image"));
                        } catch(Exception e){
                         // Print out the exception that occurred
                        System.out.println("Unable to locate image directory"+e.getMessage());
                        }
                     String output = null;
                     for(int i=0;i<(test.visitAllFiles(dir)).length;i++){
                       output=test.visitAllFiles(dir)[i]; %>
                       <option value="<%= output %>"  ><%= output %></option>

                       <% 
                      }
                     %>
            </select>
        </p>

	<p>	- Please select image file with the following file extensions(.gif's, .jpg's, .jpeg's, or .png's)<br>
		- Make sure you uploaded the image file into oscar,if the picture does not <br> appear in the list please upload it.</p>
	<p><b>Orientation of form:</b><br>
			<input type="radio" name="Orientation" id="OrientPortrait" value="750" checked>Portrait (image width should be 1500 pixels, resized to 750 pixels on screen)<br>
			<input type="radio" name="Orientation" id="OrientLandscape" value="1000">Landscape (image width should be 2000 pixels, resized to 1000 pixels on screen)<br>
			<input type="radio" name="Orientation" id="OrientCustom" value="CustomWidth">Custom <input type="text" name="OrientCustomValue" id="OrientCustomValue" width="100"> (enter an integer)
		<br>
                <input type="button" value="Load Image" onClick="loadImage();">
	</p>
	<p>If the eform image extends past the red outline, you've cropped the image too long and it won't fit on a letter-sized printout.  Try typing a number smaller than 750 in the "Custom" field.</p>
<hr>
<h2>2. eForm Name:</h2>
	<p>Enter a name for the form here:<input type="text" name="eFormName" id="eFormName"></p>
<hr>
<h2>3. Add in form input fields (one-by-one)</h2>
	<h3>a) Select an input type</h3>
		<p>
		<input type="radio" name="inputType" id="Text" value="text" onclick="SetSwitchOn(this.id);" checked>Single-line text input
		<input type="radio" name="inputType" id="Textbox" value="textarea" onclick="SetSwitchOn(this.id);">Multi-line text input
		<input type="radio" name="inputType" id="Checkbox" value="checkbox" onclick="SetSwitchOn(this.id);">Checkbox
		</p>

	<h3>b) Auto-populating Input Box</h3>
		<p>Custom text:<input type="text" name="inputValue" id="inputValue" value="">
			<br>, or <br>
			From Oscar Database:
                       <p><select name="oscarDB" id="oscarDB">
                                 <option value=""          >----None----</option>
                                <%
                                  EFormLoader names = EFormLoader.getInstance();
                                  //return the array with a list of names from database
                                  List<String> kout = names.getNames();
                                for(String str :kout){ %>
                                  <option value="<%= str %>"  ><%= str %></option>
                                   <%
                                  }
                                 %>
                        </select>
			<br>, or<br>
			Pre-check the checkbox<input name="preCheck" id="preCheck" type="checkbox">
		</p>

	<h3>c) Input Box Parameters</h3>
			<p>
			Font Family:
				<select id="fontFamily">
					 <option value="sans-serif">sans-serif</option>
					 <option value="serif">serif</option>
					 <option value="monospace">monospace</option>
				</select>
			Font Style:
				<select id="fontStyle">
					 <option value="normal">normal</option>
					 <option value="italic">italic</option>
				</select>
			Font Weight:
				<select id="fontWeight">
					 <option value="normal">normal</option>
					 <option value="bold">bold</option>
					 <option value="bolder">bolder</option>
					 <option value="lighter">lighter</option>
				</select>
			<br>
			Font Size:<input type="text" name="fontSize" id="fontSize"  style="width:50px" value="12">(in px, usually 12-14)
			Horizontal Alignment:
				<select id="textAlign">
					<option value="left">Left</option>
					<option value="center">Center</option>
					<option value="right">Right</option>
					<option value="justify">Justify</option>
				</select>
			</p>
			<p>Background Color:
				<select id="bgColor">
					<option value="transparent">transparent</option>
					<option value="white">white</option>
				</select><br>
				- Useful to have a white background to cover-up lines within the input field
			</p>
	<h3>c) Naming the input field:</h3>
		<p>i) <input type="radio" name="InputNameType" id="InputNameAuto" value="Auto" checked>Automatic Sequential Naming (Use this quicker method for most cases)
				<br>
			ii)<input type="radio" name="InputNameType" id="InputNameCustom" value="Custom">Custom <font style="color:red;">UNIQUE</font> Name:
				<input type="text" name="inputName" id="inputName">
				<br>
				- the data won't be saved properly if the name is repeated in another input field<br>
				- Must be <i>one continuous word</i> with letters/numbers only (<i>no spaces/symbols</i>)<br>
				- Use custom naming to easily identify the corresponding html code if you're going to be modifying the code later on
		</p>
	<h3>d) Drawing the input fields</h3>
		<h4>For one- and multi-lined textboxes:</h4>
			<p>
			- Click the top left corner of the intended box<br>
			- DRAG the mouse to the bottom right corner of the intended box, and let go of mouse button<br>
			- That's it!  You should see a box where the textbox will appear.
			</p>
		<h4>For checkboxes</h4>
			<p>
			- Click on the outside top left corner of the intended checkbox<br>
			- That's it!
			</p>
	<p><input type="button" onclick="Undo();" value="Undo"></p>
	<p>Repeat step # 3 until all input boxes are done.  Please leave the gender selection boxes out for now.</p>

<hr>
<h2>4. Special Case With Gender Checkboxes:</h2>eloadimages: []
			<p>Gender checkboxes used in this form?<input name="preCheckGender" id="preCheckGender" type="checkbox">
			<br>
			<b>Male</b>:
				<input name="Male" id="Male" type="button" value="Click this, then click top left corner of male checkbox" onclick="SetSwitchOn(this.id);">

			<br>
			<b>Female</b>:
				<input name="Female" id="Female" type="button" value="Click this, then click top left corner of female checkbox" onclick="SetSwitchOn(this.id);">
			</p>
<hr>
<h2>4. Check this if you want to maximize the window when this eForm loads.<input name="maximizeWindow" id="maximizeWindow" type="checkbox"></h2>
		<p>- Useful if your monitor is set at a lower resolution</p>
<hr>
<h2>5.Check this if you want to draw in checkmarks for the printouts by default.<input name="DefaultCheckmark" id="DefaultCheckmark" type="checkbox"></h2>
<hr>
<h2>6. Generate eForm</h2>
	<input name="loadHTMLButton" id="loadHTMLButton" type="button" value="Load HTML code in new window" onClick="injectHtml();">
	<input name="reset" id="reset" type="button" value="Reset form and start again" onclick="resetAll();">
	<p>- The html code should open up in Edit E-Form window.
                <br>- Now you need to fill the fields shown (<i><b>form name,Additional Information,etc</b></i>):
		<br>- Save the form by clicking <i><b>Save</b></i> button
                <br>- DONE!!
	</p>
	

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
var StrokeColor = "black";
var StrokeThickness = 2;
var x0 = 0;
var y0 = 0;

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
var MaleSwitch = false;
var FemaleSwitch = false;

function SetSwitchesOff(){
	TextSwitch = false;
	TextboxSwitch = false;
	CheckboxSwitch = false;
	MaleSwitch = false;
	FemaleSwitch = false;
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
	}else if (n=="Male"){
		MaleSwitch = true;
	}else if (n=="Female"){
		FemaleSwitch = true;
	}
}


function SetStart(){
	x0 = parseInt(mousex);	//assign x coordinate at mousedown to x0
	y0 = parseInt(mousey);	//assign y coordinate at mousedown to y0
}

function DrawText(canvas,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue){
	// draw Rectangle
	var x0 = parseInt(x0);
	var y0 = parseInt(y0);
	var width = parseInt(width);
	var height = parseInt(height);
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	canvas.drawRect(x0,y0,width,height);
	canvas.paint();

	//store parameters in an array (using separator "|")
	if (canvas == jg){
		var Parameter = "Text" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName + "|" + fontFamily + "|" + fontStyle + "|" + fontWeight + "|" + fontSize + "|" + textAlign + "|" + bgColor + "|" + oscarDB + "|" + inputValue;
		DrawData.push(Parameter);
	}
}

function DrawTextbox(canvas,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue){
	// draws Rectangle
	var x0 = parseInt(x0);
	var y0 = parseInt(y0);
	var width = parseInt(width);
	var height = parseInt(height);
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	canvas.drawRect(x0,y0,width,height);
	canvas.paint();

	//store parameters in an array (using separator "|")
	if (canvas == jg){
		var Parameter = "Textbox" + "|" + x0 + "|" + y0 + "|" + width + "|" + height + "|" + inputName + "|" + fontFamily + "|" + fontStyle + "|" + fontWeight + "|" + fontSize + "|" + textAlign + "|" + bgColor + "|" + oscarDB + "|" + inputValue;
		DrawData.push(Parameter);
	}
}

function DrawCheckbox(canvas,x,y,inputName,preCheck){
	// draws Checkbox
	var x = parseInt(x);
	var y = parseInt(y);
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	var s = 10; 	//square with side of 10
	canvas.drawRect(x,y,s,s);
	canvas.paint();
	//store parameters in an array (using separator "|")
	if (canvas == jg){
		var Parameter = "Checkbox" + "|" + x + "|" + y + "|" + inputName + "|" + preCheck;
		DrawData.push(Parameter);
	}
}

function DrawMale(canvas,x,y){
	// draws Checkbox
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	var s = 10;
	canvas.drawRect(x,y,s,s);
	canvas.paint();

	//assigns coordinates of top left corner of checkbox
	MTopLeftX = x;
	MTopLeftY = y;
}

function DrawFemale(canvas,x,y){
	// draws Checkbox
	canvas.setColor(StrokeColor);
	canvas.setStroke(StrokeThickness);
	var s = 10;
	canvas.drawRect(x,y,s,s);
	canvas.paint();

	//assigns coordinates of top left corner of checkbox
	FTopLeftX = x;
	FTopLeftY = y;

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
	var textAlign = document.getElementById('textAlign').value;
	var bgColor = document.getElementById('bgColor').value;
	var oscarDB = document.getElementById('oscarDB').value;
	var inputValue = document.getElementById('inputValue').value;
	var preCheck = document.getElementById('preCheck').checked

	//get name of input field
	var inputNameType = getCheckedValue(document.getElementsByName('InputNameType'));  // inputNameType = Auto/Custom
	if (inputNameType == "Custom"){
		e = document.getElementById('inputName').value
		if (e){
			inputName = document.getElementById('inputName').value;
		} else if (!e){
			alert('Please enter in a value for the custom input name field');	//reminds user to put in mandatory name for input field
			return false;
		}
	} else if (inputNameType == "Auto") {
		inputName = 'AutoName' + inputCounter;
		++inputCounter;
	}


	if(DrawSwitch){
		if (TextSwitch){
			DrawText(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue);
		}else if (TextboxSwitch){
			DrawTextbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue);
		}else if (CheckboxSwitch){
			DrawCheckbox(jg,x,y,inputName,preCheck);
		}else if(MaleSwitch){
			DrawMale(jg,x,y);
		}else if(FemaleSwitch){
			DrawFemale(jg,x,y);
		}
	}

	//reset input data
	document.getElementById('inputValue').value = "";
	document.getElementById('inputName').value = "";
	document.getElementById('preCheck').checked = false;
	var l = document.getElementById('oscarDB');
		l[0].selected = true;
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
		DrawText(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue);
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
		DrawTextbox(jg,x0,y0,width,height,inputName,fontFamily,fontStyle,fontWeight,fontSize,textAlign,bgColor,oscarDB,inputValue);
	}else if (InputType == "Checkbox"){
		var x = parseInt(RedrawParameter[1]);
		var y = parseInt(RedrawParameter[2]);
		var inputName = RedrawParameter[3];
		var preCheck = RedrawParameter[4];
		DrawCheckbox(jg,x,y,inputName,preCheck);
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
</body>
</html>