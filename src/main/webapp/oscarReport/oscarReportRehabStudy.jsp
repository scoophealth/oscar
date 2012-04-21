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

<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%
GregorianCalendar now = new GregorianCalendar();
GregorianCalendar cal = (GregorianCalendar) now.clone();
String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
%>
<%@ page import="java.util.*,oscar.oscarReport.data.*, java.net.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">
<script language="JavaScript" type="text/JavaScript">
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);

function findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function showHideLayers() { //v6.0
  var i,p,v,obj,args=showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</script>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rehab Study Reports</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<style type="text/css" media="print">
.header {
	display: none;
}

.header INPUT {
	display: none;
}

.header A {
	display: none;
}
</style>
<script type="text/javascript">
   function generateResult(formName){
        document.forms[0].formName.value=formName;
        document.forms[0].submit();
    }
   
</script>

</head>

<body vlink="#0000FF" class="BodyStyle">
<div id="Layer1"
	style="position: absolute; left: 5px; top: 200px; width: 800px; height: 600px; z-index: 1; visibility: hidden;">
<table width="100%" border="1" cellpadding="0" cellspacing="0"
	bgcolor="#D6D5C5">
	<tr>
		<td><font size="2" face="Tahoma"> <logic:present
			name="resultText">
			<pre><bean:write name="resultText" filter="false" /></pre>
		</logic:present> </font></td>
	</tr>
</table>
</div>
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<html:form action="/oscarReport/RptRehabStudy.do">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarReport.CDMReport.msgReport" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td width="25%"><logic:present name="formName">
						<bean:write name="formName" />
					</logic:present></td>
					<td><a HREF="#"
						onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../oscarReport/oscarReportRehabStudy.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.forms[0].startDate.value")%>')">
					<bean:message key="report.reportindex.formFrom" /></a> <INPUT
						TYPE="text" NAME="startDate" VALUE="" size='10'> <a
						HREF="#"
						onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../oscarReport/oscarReportRehabStudy.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.forms[0].endDate.value")%>')">
					<bean:message key="report.reportindex.formTo" /></a> <INPUT TYPE="text"
						NAME="endDate" size='10'></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn" valign="top"><html:hidden
				property="formName" value="" />
			<table>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('form2MinWalk');">2 Minutes Walk</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#" onClick="generateResult('formCESD');">CESD</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formCaregiver');">Caregiver</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formCostQuestionnaire');">Cost
					Questionnaire</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#" onClick="generateResult('formFalls');">Falls
					History</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formGripStrength');">Grip Strength</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formHomeFalls');">Home Fast</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formIntakeInfo');">Intake</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formLateLifeFDIDisability');">Late
					Life FDI Disability</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formLateLifeFDIFunction');">Late Life
					FDI Function</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#" onClick="generateResult('formSF36');">SF36</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formSF36Caregiver');">SF36 Caregiver</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formSelfAdministered');">Self
					Administered</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formSelfEfficacy');">Self Efficacy</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formSelfManagement');">Self
					Management</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formTreatmentPref');">Treatment
					Preference</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formInternetAccess');">Internet
					Access</a></td>
				</tr>
				<tr>
					<td nowrap><a href="#"
						onClick="generateResult('formSatisfactionScale');">Satisfaction
					Scale</a></td>
				</tr>


			</table>
			</td>
			<td class="MainTableRightColumn" valign="top"><logic:present
				name="results">
				<bean:write name="results" filter="false" />
			</logic:present></td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

			<td class="MainTableBottomRowRightColumn">&nbsp;</td>
		</tr>
</table>

</html:form>
</body>
</html:html>
