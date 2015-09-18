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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%
    String formClass = "2MinWalk";
    String formLink = "form2minwalk.jsp";

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
<title>2 Minute Walk Test and Lower Extremity Function Test</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(7,11,16,20);        
    var allNumericField = null;
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";
    
    function checkBeforeSave(){
        var distance = document.forms[0].elements[6].value;
        var re1 = new RegExp('^[0-9][0-9][0-9][0-9][\.][0-9]$');
        var re2 = new RegExp('^[0-9][0-9][0-9][\.][0-9]$');
        var re3 = new RegExp('^[0-9][0-9][\.][0-9]$');
        var re4 = new RegExp('^[0-9][\.][0-9]$');
        var match1 = document.forms[0].elements[6].value.match(re1);
        var match2 = document.forms[0].elements[6].value.match(re2);
        var match3 = document.forms[0].elements[6].value.match(re3);
        var match4 = document.forms[0].elements[6].value.match(re4);
        if(match1||match2||match3||match4){            
            if(isFormCompleted(6,21,2,3)==true){
                return true;
            }    
        }
        else{
            alert("The input distance must be in ####.# format");
            return false;
        }
               
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
	<input type="hidden" name="submit" value="exit" />

	<table border="0" cellspacing="0" cellpadding="0" width="740px"
		height="95%">
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" width="740px"
				height="10%">
				<tr>
					<th class="subject">2 Minute Walk Test and Lower Extremity
					Function Test</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table border="0" cellspacing="0" cellpadding="0" height="85%"
				width="740px" id="page1">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="5">2 Minute Walk</th>
						</tr>
						<tr>
							<td valign="top" align="right">
							<li></li>
							</td>
							<td valign="top" colspan="4">"Please walk as quickly and as
							safely as possible for 2 minutes. I will let you know when the
							time is up."</td>
						</tr>
						<tr>
							<td valign="top" align="right">
							<li></li>
							</td>
							<td valign="top" colspan="4">Assessor should time and
							measure the distance the respondent walks</td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="4">Distance: <input type="text" size="10"
								name="distance" value="<%= props.getProperty("distance", "") %>" />
							meter</td>
						</tr>
						<tr class="title">
							<th colspan="5">Lower Extremity Function Test</th>
						</tr>
						<tr>
							<th>1.</th>
							<th colspan="4" class="question">Balance</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q1tried"
								<%= props.getProperty("Q1tried", "") %> /></td>
							<td width="40%">Tried but unable</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q1FullTandem3To9"
								<%= props.getProperty("Q1FullTandem3To9", "") %> /></td>
							<td width="55%">3-9 secs. full tandem</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q1SideBySide10"
								<%= props.getProperty("Q1SideBySide10", "") %> /></td>
							<td width="40%">10 secs. side by side</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q1FullTandem10"
								<%= props.getProperty("Q1FullTandem10", "") %> /></td>
							<td width="55%">10 secs. full tandem</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q1SemiTandem10"
								<%= props.getProperty("Q1SemiTandem10", "") %> /></td>
							<td width="40%">10 secs semi tandem</td>
							<td width="5%"></td>
							<td width="55%"></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="4">Comments: <input type="text" size="80"
								name="Q1Cmt" value="<%= props.getProperty("Q1Cmt", "") %>" /></td>
						</tr>
						<tr>
							<th>2.</th>
							<th colspan="4" class="question">Measured Walk (May use
							walking aids)</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="4">
							<li>Measure 8 feet (But allow 10 feet in which they can
							walk)</li>
							<li>"I want you to walk at your usual speed. Ready begin"</li>
							<li>When the respondent starts, begin timing*</li>
							<li>Record the time in the number of seconds</li>
							<li>Repeat walk</li>
							<br>
							* Should begin with feet together and end when both feet past the
							end of the ruler</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="2">Time 1:<input type="text" size="10"
								name="Q2time1" value="<%= props.getProperty("Q2time1", "") %>" /></td>
							<td width="5%" align="right"></td>
							<td width="55%"></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="2">Time 2:<input type="text" size="10"
								name="Q2time2" value="<%= props.getProperty("Q2time2", "") %>" /></td>
							<td width="5%" align="right"></td>
							<td width="55%"></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="4">Comments: <input type="text" size="80"
								name="Q2Cmt" value="<%= props.getProperty("Q2Cmt", "") %>" /></td>
						</tr>
						<tr>
							<th>3.</th>
							<th colspan="4" class="question">Chair Stands</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="4">
							<li>Armless at front and straight backed chair 18" high and
							16" deep</li>
							<li>Stand 5X from sit to standing, ending with standing</li>
							<li>If they are not able to stand unaided, code "Can't
							stand"</li>
							<li>Record why the respondent thinks it would be unsafe</li>
							</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q3Unable"
								<%= props.getProperty("Q3Unable", "") %> /></td>
							<td width="40%">Unable to stand without using arms</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q3From11To13s"
								<%= props.getProperty("Q3From11To13s", "") %> /></td>
							<td width="55%">(11.2 - 13.6 seconds)</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q3LessThan16s"
								<%= props.getProperty("Q3LessThan16s", "") %> /></td>
							<td width="40%">(Greater than or equal to 16.7 seconds)</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q3LessThan11s"
								<%= props.getProperty("Q3LessThan11s", "") %> /></td>
							<td width="55%">(Less than or equal to 11.1 seconds)</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="Q3From13To16s"
								<%= props.getProperty("Q3From13To16s", "") %> /></td>
							<td width="40%">(13.7 - 16.6 seconds)</td>
							<td width="5%"></td>
							<td width="55%"></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td colspan="4">Comments: <input type="text" size="80"
								name="Q3Cmt" value="<%= props.getProperty("Q3Cmt", "") %>" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>

		<tr>
			<td valign="top">
			<table class="Head" class="hidePrint" height="5%">
				<tr>
					<td align="left">
					<%
  if (!bView) {
%> <input type="submit" value="Save"
						onclick="javascript: if(checkBeforeSave()==true) return onSave(); else return false;" />
					<input type="submit" value="Save and Exit"
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
