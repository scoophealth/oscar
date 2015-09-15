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
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%
    String formClass = "SelfAdministered";
    String formLink = "formselfadministered.jsp";

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
<title>Self Administered Questions Used in Self-Report Risk
Index</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(6,10,11,14,15,18,19,20,21,22,23,24,25,26,27,28,29,30);    
    var allMatch = null;
    var allNumericField = null;
    var action = "/<%=project_home%>/form/formname.do";

    function checkBeforeSave(){                        
        if(isFormCompleted(6,30,9,0)==true)
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

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align='LEFT'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">Self Administered Questions Used in
			Self-Report Risk Index</font></th>
		</tr>
	</table>

	<table>
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">1.</td>
					<td>Would you say, in general your health is</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="healthEx"
						<%= props.getProperty("healthEx", "") %> /></td>
					<td>Excellent</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="healthVG"
						<%= props.getProperty("healthVG", "") %> /></td>
					<td>Very Good</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="healthG"
						<%= props.getProperty("healthG", "") %> /></td>
					<td>Good</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="healthF"
						<%= props.getProperty("healthF", "") %> /></td>
					<td>Fair</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="healthP"
						<%= props.getProperty("healthP", "") %> /></td>
					<td>Poor</td>
				</tr>
				<tr>
					<td align="right">2.</td>
					<td>In the previous 12 months have you stayed overnight as a
					patient in a hospital?</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="stayInHospNo"
						<%= props.getProperty("stayInHospNo", "") %> /></td>
					<td>Not at all</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="stayInHosp1"
						<%= props.getProperty("stayInHosp1", "") %> /></td>
					<td>One time</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="stayInHosp2Or3"
						<%= props.getProperty("stayInHosp2Or3", "") %> /></td>
					<td>Two or three times</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="stayInHospMore3"
						<%= props.getProperty("stayInHospMore3", "") %> /></td>
					<td>More than three times</td>
				</tr>
				<tr>
					<td align="right">3.</td>
					<td>In the previous 12 months, how many times did you visit a
					physician or clinic?</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="visitPhyNo"
						<%= props.getProperty("visitPhyNo", "") %> /></td>
					<td>Not at all</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="visitPhy1"
						<%= props.getProperty("visitPhy1", "") %> /></td>
					<td>One time</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="visitPhy2Or3"
						<%= props.getProperty("visitPhy2Or3", "") %> /></td>
					<td>Two or three times</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="visitPhyMore3"
						<%= props.getProperty("visitPhyMore3", "") %> /></td>
					<td>More than three times</td>
				</tr>
				<tr>
					<td align="right">4.</td>
					<td>In the previous 12 months, did you have diabetes?</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="diabetesY"
						<%= props.getProperty("diabetesY", "") %> /></td>
					<td>Yes</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="diabetesN"
						<%= props.getProperty("diabetesN", "") %> /></td>
					<td>No</td>
				</tr>
				<tr>
					<td align="right">5.</td>
					<td>Have you ever had ...</td>
				</tr>
				<tr bgcolor="white">
					<td colspan="2">
					<table bgcolor="white">
						<tr>
							<td width="30%">Coronary Heart Disease</td>
							<td width="20%">Angina Pectoris</td>
							<td width="25%">Myocardial infarction</td>
							<td width="25%">Any other heart attack</td>
						</tr>
						<tr>
							<td align="left"><input type="checkbox" class="checkbox"
								name="heartDiseaseY"
								<%= props.getProperty("heartDiseaseY", "") %> /> Yes <br>
							<input type="checkbox" class="checkbox" name="heartDiseaseN"
								<%= props.getProperty("heartDiseaseN", "") %> /> No</td>
							<td align="left"><input type="checkbox" class="checkbox"
								name="anginaPectorisY"
								<%= props.getProperty("anginaPectorisY", "") %> /> Yes<br>
							<input type="checkbox" class="checkbox" name="anginaPectorisN"
								<%= props.getProperty("anginaPectorisN", "") %> /> No</td>
							<td align="left"><input type="checkbox" class="checkbox"
								name="myocardialInfarctionY"
								<%= props.getProperty("myocardialInfarctionY", "") %> /> Yes<br>
							<input type="checkbox" class="checkbox"
								name="myocardialInfarctionN"
								<%= props.getProperty("myocardialInfarctionN", "") %> /> No</td>
							<td align="left"><input type="checkbox" class="checkbox"
								name="anyHeartAttackY"
								<%= props.getProperty("anyHeartAttackY", "") %> /> Yes<br>
							<input type="checkbox" class="checkbox" name="anyHeartAttackN"
								<%= props.getProperty("anyHeartAttackN", "") %> /> No</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">6.</td>
					<td>Your sex is: <%= props.getProperty("sex", "") %></td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right">&nbsp;</td>
					<td></td>
				</tr>
				<tr>
					<td align="right" valign="top">7.</td>
					<td>Is there a friend, relative neighbour who would take care
					of you for a few days if neccessary?</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="relativeTakeCareY"
						<%= props.getProperty("relativeTakeCareY", "") %> /></td>
					<td>Yes</td>
				</tr>
				<tr bgcolor="white">
					<td width="5%" align="right"><input type="checkbox"
						class="checkbox" name="relativeTakeCareN"
						<%= props.getProperty("relativeTakeCareN", "") %> /></td>
					<td>No</td>
				</tr>
				<tr>
					<td align="right" valign="top">8.</td>
					<td>Your birth date is: <%= props.getProperty("dob", "") %></td>
				</tr>
			</table>
			</td>
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
				value="<%= props.getProperty("studyID", "N/A") %>" /> <input
				type="hidden" name="sex" value="<%= props.getProperty("sex", "") %>" />
			<input type="hidden" name="dob"
				value="<%= props.getProperty("dob", "") %>" /></td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
