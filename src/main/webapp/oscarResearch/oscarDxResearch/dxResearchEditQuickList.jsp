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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%   

    String user_no = (String) session.getAttribute("user");
    String color ="";
    int Count=0;    
%>

<link rel="stylesheet" type="text/css" href="dxResearch.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarResearch.oscarDxResearch.dxResearch.title" /></title>
<script language="JavaScript">
<!--
function setfocus() {
        window.focus();
        window.resizeTo(650,400);
	document.forms[0].xml_research1.focus();
	document.forms[0].xml_research1.select();
}

var remote=null;

function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
			remote.opener = self;
	}
	if (x == 1) { return remote; }
}

function popPage(url) {
	awnd=rs('',url ,400,200,1);
	awnd.focus();
}

var awnd=null;

function ResearchScriptAttach() {
	var t0 = escape(document.forms[0].xml_research1.value);
	var t1 = escape(document.forms[0].xml_research2.value);
	var t2 = escape(document.forms[0].xml_research3.value);
	var t3 = escape(document.forms[0].xml_research4.value);
	var t4 = escape(document.forms[0].xml_research5.value);
        var codeType = document.forms[0].selectedCodingSystem.value;
	awnd=rs('att','dxResearchCodeSearch.do?codeType='+codeType + '&xml_research1='+t0 + '&xml_research2=' + t1 + '&xml_research3=' + t2 + '&xml_research4=' + t3 + '&xml_research5=' + t4 +'&demographicNo=',600,600,1);
	awnd.focus();
}

function submitform(target){	
        document.forms[0].forward.value = target;
	document.forms[0].submit()
}

function set(target) {
     document.forms[0].forward.value=target;
}


function openNewPage(vheight,vwidth,varpage) { 
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.Index.msgOscarConsultation"/>", windowprops);
  popup.focus();
}

//-->
</SCRIPT>
</head>

<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0"
	topmargin="0" marginwidth="0" marginheight="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr bgcolor="#000000">
				<td class="subject" colspan="2">&nbsp;&nbsp;&nbsp;<bean:message
					key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><html:form
			action="/oscarResearch/oscarDxResearch/dxResearchUpdateQuickList.do">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				bgcolor="#EEEEFF" height="200">
				<tr>
					<td class="heading"><bean:message
						key="oscarResearch.oscarDxResearch.codingSystem" />: <%-- <bean:write name="codingSystem"/> --%>
					<html:select property="selectedCodingSystem">
						<logic:iterate id="codingSys" name="codingSystem"
							property="codingSystems">
							<option value="<bean:write name="codingSys"/>"><bean:write
								name="codingSys" /></option>
						</logic:iterate>
					</html:select></td>
					<td class="heading"></td>
					<td class="heading"><bean:message
						key="oscarResearch.oscarDxResearch.quickListItemsOf" /> <bean:write
						name="quickListName" /> <input type="hidden" name="quickListName"
						value="<bean:write name="quickListName"/>" /></td>
				</tr>
				<tr>
					<td colspan="3"><html:errors /></td>
				</tr>
				<tr>
					<td width="40%" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="2"
						height="200">
						<tr>
							<td><html:text property="xml_research1" size="30" /></td>
						</tr>
						<tr>
							<td><html:text property="xml_research2" size="30" /></td>
						</tr>
						<tr>
							<td><html:text property="xml_research3" size="30" /></td>
						</tr>
						<tr>
							<td><html:text property="xml_research4" size="30" /></td>
						</tr>
						<tr>
							<td><html:text property="xml_research5" size="30" /></td>
						</tr>
						<tr>
							<td><input type="button" name="button" class=mbttn
								value="<bean:message key="oscarResearch.oscarDxResearch.btnCodeSearch"/>"
								onClick="javascript: ResearchScriptAttach();")></td>
						</tr>
					</table>
					</td>
					<td>
					<table>
						<tr>
							<td><input type="hidden" name="forward" value="none" /> <input
								type="button" name="button" class=mbttn style="width: 80"
								value="<bean:message key="ADD"/> >>"
								onClick="javascript: submitform('add');"></td>
						</tr>
						<tr>
							<td><input type="button" name="button" class=mbttn
								style="width: 80"
								value="<< <bean:message key="REMOVE"/>"
								onClick="javascript: submitform('remove');"></td>
						</tr>
					</table>
					</td>
					<td valign="top">
					<table>
						<tr>
							<td><html:select property="quickListItems"
								style="width:200px" size="10" multiple="true">
								<logic:iterate id="qlItems" name="allQuickListItems"
									property="dxQuickListItemsVector">
									<option
										value="<bean:write name="qlItems" property="type" />,<bean:write name="qlItems" property="dxSearchCode" />"><bean:write
										name="qlItems" property="description" /></option>
								</logic:iterate>
							</html:select></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><input type="button" class="mbttn" name="Button"
						value="<bean:message key="global.btnClose"/>"
						onClick="window.close()"></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>
		</html:form></td>
	</tr>
</table>
</body>
</html:html>
