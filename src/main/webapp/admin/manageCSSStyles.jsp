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

<%@include file="/casemgmt/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
<head>
<title><bean:message key="admin.admin.manageCodeStyles"/></title>
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js" type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js" type="text/javascript"></script>
<script type="text/javascript" src="../share/javascript/picker.js"></script>
<script type="text/javascript">
<%	
	String success = (String)request.getAttribute("success");
	if( "true".equalsIgnoreCase(success)) {
%>
		alert("<bean:message key="admin.manageCodeStyles.sucess"/>");
<%
	}
%>

function enableEdit(elem) {
	if( elem.checked == true ) {
		$("styleText").readOnly = false;
	}
	else {
		$("styleText").readOnly = true;
	}
	
}

function addStyle(id, option) {		
	var currentStyle = $F("styleText");
	var idx = currentStyle.indexOf(id);
	var idx2;
	var tmp1;
	var tmp2;
	
	//need to account for color not overwriting background-color
	if(id == "color") {
		tmp1 = currentStyle.charAt(idx-1);		
		if( tmp1 == '-') {
			idx = currentStyle.indexOf(id,idx+1);			
		}
	}
	
	if( idx != -1 ) {		
		tmp1 = currentStyle.substring(0,idx);
		idx2 = currentStyle.indexOf(";", idx);
		tmp2 = currentStyle.substring(idx2+1);
		
		if( option.value != "" ) {
			currentStyle = tmp1 + id + ":" + option.value + ";" + tmp2;
		}
		else {
			currentStyle = tmp1 + tmp2;
		}		
		
		$("styleText").value = currentStyle;
		$("example").style.cssText = currentStyle;
	}
	else {
		if( option.value != "" ) {
			currentStyle += id + ":" + option.value + ";";
			$("styleText").value = currentStyle;
			$("example").style.cssText = currentStyle;
		}
	}
	
	
}

var color;
var bgcolor;
function checkColours() {
	if( color != $F("color") ) {		
		addStyle("color", $("color"));	
		color = $F("color");
	}
	
	if( bgcolor != $F("background-color") ) {		
		addStyle("background-color", $("background-color"));
		bgcolor = $F("background-color");
	}	
}

function edit() {
	var style = $("style").options[$("style").selectedIndex].value;
	var styles = style.split(";");
	var item;
	var components;
	var value;
	var pos;
	var tmp;
	
	$("font-size").selectedIndex = 0;
	$("font-style").selectedIndex = 0;
	$("font-variant").selectedIndex = 0;
	$("font-weight").selectedIndex = 0;
	$("text-decoration").selectedIndex = 0;
	$("styleName").value = "";
	$("color").value = "";
	$("background-color").value = "";	
	$("styleText").value = "";
	
	for( var idx = 0; idx < styles.length-1; ++idx) {
		components = styles[idx].split(":");
		item = components[0];
		value = components[1];		
		
		if( item == "color" || item == "background-color" ) {
			$(item).value = value;
		}
		else {
			for( var idx2 = 0; idx < $(item).options.length; ++idx2 ) {
				if( $(item).options[idx2].value == value ) {
					$(item).options[idx2].selected = true;
					break;
				}
			} //end for
		} 
	} //end for
	
	if( style != "-1" ) {
		$("styleText").value = style;
		$("editStyle").value = style;
		$("example").style.cssText= style;
		$("styleName").value = $("style").options[$("style").selectedIndex].text;
	}
	
}

function checkfields() {
	var msg = "";
	
	if( $("styleText").value.length == 0 ) {
		msg = "<bean:message key="admin.manageCodeStyles.noStyleError"/>";
	}
	
	if( $("styleName").value.trim().length == 0 ) {
		msg += "\r\n<bean:message key="admin.manageCodeStyles.noStyleNameError"/>";
	}
	
	if( msg.length > 0 ) {
		alert(msg);
		return false;
	}
	
	//if it's a new style save it for addition
	if( $("style").selectedIndex == 0 ) {
		$("editStyle").value = $("styleText").value;
	}
	$("method").value = "save";
	
	return true;
	
}

function deleteStyle() {
	
	if( $("style").selectedIndex == 0 ) {
		return false;
	}
	
	if( confirm("<bean:message key="admin.manageCodeStyles.confirmDelete"/>")) {
		$("editStyle").value = $("style").options[$("style").selectedIndex].value;
		$("method").value = "delete";
		return true;
	}
	return false;	
}

function applyStyle() {
	$("example").style.cssText = $("styleText").value;
}

function reinit() {		
	$("style").selectedIndex = 0;
	$("font-size").selectedIndex = 0;
	$("font-style").selectedIndex = 0;
	$("font-variant").selectedIndex = 0;
	$("font-weight").selectedIndex = 0;
	$("text-decoration").selectedIndex = 0;
	$("styleName").value = "";
	$("color").value = "";
	$("background-color").value = "";	
	$("styleText").value = "";
	$("editStyle").value = "";
	$("example").style.cssText="";
}

function init() {
	reinit();
	color = $F("color");
	bgcolor = $F("background-color");
	setInterval("checkColours()",5000);	
}

</script>
</head>
<body bgcolor="ivory" onload="init();">

<html:form action="/admin/manageCSSStyles" method="post">
<input type="hidden" id="method" name="method" value="save"/>
<html:hidden styleId="editStyle" property="editStyle"/>
<div style="float:right;text-align:center;margin-right:40px;"><bean:message key="admin.manageCodeStyles.StyleText"/><br/><span style="font-size:small;"><bean:message key="admin.manageCodeStyles.ManualEnter"/><input type="checkbox" onclick="enableEdit(this);"></span><br/><html:textarea rows="5" cols="90" readonly="true" styleId="styleText" property="styleText"></html:textarea></div>
<div style="float:left;text-align:center;"><bean:message key="admin.manageCodeStyles.CurrentStyles"/><br/><span style="font-size:small;">&nbsp;</span><br/><html:select styleId="style" property="selectedStyle"><option value="-1"><bean:message key="admin.manageCodeStyles.NoneSelected"/></option><html:optionsCollection property="styles" label="name" value="style"></html:optionsCollection></html:select>&nbsp;<span><input type="button" onclick="edit();return false;" value="<bean:message key="admin.manageCodeStyles.Edit"/>"/><br/><br/>&nbsp;<html:submit value="Delete" onclick="return deleteStyle();"></html:submit></span></div>
<br clear="all"/>
<div style="text-align:center;"><input type="button" value="<bean:message key="admin.manageCodeStyles.Clear"/>" onclick="reinit();return false;"/>&nbsp;&nbsp;<input type="button" value="<bean:message key="admin.manageCodeStyles.Apply"/>" onclick="applyStyle();return false;"/><br><span id="example"><bean:message key="admin.manageCodeStyles.Example"/></span></div>
<hr style="width:33%; text-align:center;"/>
<div style="text-align:center;">
<bean:message key="admin.manageCodeStyles.StyleName"/>&nbsp;<html:text styleId="styleName" property="styleName"></html:text>
<p><bean:message key="admin.manageCodeStyles.Instructions"/></p>
<bean:message key="admin.manageCodeStyles.FontSize"/>&nbsp;
<select id="font-size" onchange="addStyle(this.id, this.options[this.selectedIndex]);">
<option value=""><bean:message key="admin.manageCodeStyles.NoneSelected"/></option>
<option value="xx-small">XX-Small</option>
<option value="x-small">X-Small</option>
<option value="medium">Medium</option>
<option value="large">Large</option>
<option value="x-large">X-Large</option>
<option value="xx-large">XX-Large</option>
</select>

<bean:message key="admin.manageCodeStyles.FontStyle"/>&nbsp;
<select id="font-style" onchange="addStyle(this.id, this.options[this.selectedIndex]);">
<option value=""><bean:message key="admin.manageCodeStyles.NoneSelected"/></option>
<option value="italic">Italic</option>
<option value="oblique">Obllique</option>
</select>

<bean:message key="admin.manageCodeStyles.FontVariant"/>&nbsp;
<select id="font-variant" onchange="addStyle(this.id, this.options[this.selectedIndex]);">
<option value=""><bean:message key="admin.manageCodeStyles.NoneSelected"/></option>
<option value="small-caps">Small-Caps</option>
</select>

<bean:message key="admin.manageCodeStyles.FontWeight"/>&nbsp;
<select id="font-weight" onchange="addStyle(this.id, this.options[this.selectedIndex]);">
<option value=""><bean:message key="admin.manageCodeStyles.NoneSelected"/></option>
<option value="bold">Bold</option>
<option value="bolder">Bolder</option>
<option value="lighter">Lighter</option>
</select>
<br/><br/>

<bean:message key="admin.manageCodeStyles.TextDecoration"/>&nbsp;
<select id="text-decoration" onchange="addStyle(this.id, this.options[this.selectedIndex]);">
<option value=""><bean:message key="admin.manageCodeStyles.NoneSelected"/></option>
<option value="underline">Underline</option>
<option value="overline">Overline</option>
<option value="line-through">Line Through</option>
</select>

<bean:message key="admin.manageCodeStyles.TextColour"/>&nbsp;
<a href="javascript:TCP.popup(document.forms[0].elements['color'])"><img
				width="15" height="13" border="0" src="../images/sel.gif"></a>
<input id="color" type="text" readonly="readonly" size="7"/>

<bean:message key="admin.manageCodeStyles.BackgroundColour"/>&nbsp;
<a href="javascript:TCP.popup(document.forms[0].elements['background-color'])"><img
				width="15" height="13" border="0" src="../images/sel.gif"></a>
<input id="background-color" type="text" readonly="readonly" size="7"/>
</div>
<p style="text-align:center;">
<html:submit value="Save" onclick="return checkfields();"></html:submit>
</html:form>

</body>

</html>
