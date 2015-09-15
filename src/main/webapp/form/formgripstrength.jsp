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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
    String formClass = "GripStrength";
    String formLink = "formgripstrength.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

    //get project_home
    String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Grip Strength Measurements (Kgs)</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = null;
    var allNumericField = new Array(6,7,8,9,10,11,12,13);
    var allMatch = null;
    
    var action = "/<%=project_home%>/form/formname.do";    
    var domValues = new Array(6,8,10);
    var nonDomValues = new Array(7,9,11);
    var domResult = 12;
    var nonDomResult = 13;
    
    function calScore(values, result){
        var score = 0;
        for(var i=0; i<values.length; i++){
            var item = values[i];
            score = score + eval(document.forms[0].elements[item].value);
        }
            document.forms[0].elements[result].value=score/(values.length);        
    }
    
    function checkBeforeSave(){                        
        if(isFormCompleted(6,13,0,8)==true)
            return true;
        
        return false;
    }
</script>
<script type="text/javascript" src="formScripts.js">
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,768)">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />

	<table border="0" cellspacing="0" cellpadding="0" width="740px"
		height="710px">
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" width="740px">
				<tr>
					<th class="subject">Grip Strength Measurements (Kgs)</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table border="0" cellspacing="0" cellpadding="0" height="660px"
				width="740px" id="page1">
				<tr>
					<td valign="top" colspan="2">
					<table width="100%" height="300px" border="0" cellspacing="1px"
						cellpadding="0">
						<tr class="title">
							<th colspan="2">Time 1</th>
						</tr>
						<tr class="title">
							<th align="center"><font style="font-size: 85%">
							Dominant Limb </font></th>
							<th align="center"><font style="font-size: 85%">
							Non-Dominant Limb </font></th>
						</tr>
						<tr>
							<td bgcolor="white" align="center">1 = <input type="text"
								onchange="javascript: calScore(domValues, domResult);"
								name="dom1" value="<%= props.getProperty("dom1", "") %>" /></td>
							<td bgcolor="white" align="center">1 = <input type="text"
								onchange="javascript: calScore(nonDomValues, nonDomResult);"
								name="nonDom1" value="<%= props.getProperty("nonDom1", "") %>" />
							</td>
						</tr>
						<tr>
							<td bgcolor="white" align="center">2 = <input type="text"
								onchange="javascript: calScore(domValues, domResult);"
								name="dom2" value="<%= props.getProperty("dom2", "") %>" /></td>
							<td bgcolor="white" align="center">2 = <input type="text"
								onchange="javascript: calScore(nonDomValues, nonDomResult);"
								name="nonDom2" value="<%= props.getProperty("nonDom2", "") %>" />
							</td>
						</tr>
						<tr>
							<td bgcolor="white" align="center">3 = <input type="text"
								onchange="javascript: calScore(domValues, domResult);"
								name="dom3" value="<%= props.getProperty("dom3", "") %>" /></td>
							<td bgcolor="white" align="center">3 = <input type="text"
								onchange="javascript: calScore(nonDomValues, nonDomResult);"
								name="nonDom3" value="<%= props.getProperty("nonDom3", "") %>" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td bgcolor="white" align="center">Average = <input
								type="text" readonly="true" name="domAvg"
								value="<%= props.getProperty("domAvg", "") %>" /></td>
							<td bgcolor="white" align="center">Average = <input
								type="text" readonly="true" name="nonDomAvg"
								value="<%= props.getProperty("nonDomAvg", "") %>" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table class="Head" class="hidePrint">
				<tr>
					<td align="left">
					<%
  if (!bView) {
%> <input type="submit" value="Save"
						onclick="javascript: return onSave();" /> <input type="submit"
						value="Save and Exit"
						onclick="javascript:if(checkBeforeSave()==true) return onSaveExit(); else return false;" />
					<%
  }
%> <input type="button" value="Exit"
						onclick="javascript:return onExit();" /> <input type="button"
						value="Print" onclick="javascript:window.print();" /></td>
					<td align="right">Study ID: <%= props.getProperty("studyID", "N/A") %>
					<input type="hidden" name="studyID"
						value="<%= props.getProperty("studyID", "N/A") %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
