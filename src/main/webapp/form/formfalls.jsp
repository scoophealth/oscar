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
    String formClass = "Falls";
    String formLink = "formfalls.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

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
<title>History of Falls Questionnaire</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">

    var choiceFormat  = new Array(6,8,9,10,11,12,13,14,15,16);    
    var allNumericField = null;
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";

    function checkBeforeSave(){                        
        if(isFormCompleted(6,16,2,0)==true)
            return true;
        
        return false;
    }
</script>
<script type="text/javascript" src="formScripts.js">
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,670)">
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

	<table border="0" cellspacing="1" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align='Left'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">History of Falls Questionaire</font></th>
		</tr>
	</table>


	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="2">1. Have you fallen in the last 6 months?</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				name="fallenLast12MY" class="checkbox"
				<%= props.getProperty("fallenLast12MY", "") %> /></td>
			<td>Yes</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				name="fallenLast12MN" class="checkbox"
				<%= props.getProperty("fallenLast12MN", "") %> /></td>
			<td>No - go to #5</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="fallenLast12MNotRemember"
				<%= props.getProperty("fallenLast12MNotRemember", "") %> /></td>
			<td>Cannot Remember - go to #5</td>
		</tr>
		<tr>
			<td colspan="2">If Yes,</td>
		</tr>
		<tr>
			<td colspan="2">2. Were you injured because of your fall?</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="injuredY"
				<%= props.getProperty("injuredY", "") %> /></td>
			<td>Yes</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="injuredN"
				<%= props.getProperty("injuredN", "") %> /></td>
			<td>No</td>
		</tr>
		<tr>
			<td colspan="2">3. Did you receive medical attention due to the
			fall?</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="medAttnY"
				<%= props.getProperty("medAttnY", "") %> /></td>
			<td>Yes</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="medAttnN"
				<%= props.getProperty("medAttnN", "") %> /></td>
			<td>No</td>
		</tr>
		<tr>
			<td colspan="2">4. Were you hospitalized because of the fall?</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="hospitalizedY"
				<%= props.getProperty("hospitalizedY", "") %> /></td>
			<td>Yes</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="hospitalizedN"
				<%= props.getProperty("hospitalizedN", "") %> /></td>
			<td>No</td>
		</tr>
		<tr>
			<td colspan="2">5. Do you limit your activities because of a
			fear of falling?</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="limitActY"
				<%= props.getProperty("limitActY", "") %> /></td>
			<td>Yes</td>
		</tr>
		<tr bgcolor="white">
			<td width="5%" align="right"><input type="checkbox"
				class="checkbox" name="limitActN"
				<%= props.getProperty("limitActN", "") %> /></td>
			<td>No</td>
		</tr>
	</table>


	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
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


</html:form>
</body>
</html:html>
