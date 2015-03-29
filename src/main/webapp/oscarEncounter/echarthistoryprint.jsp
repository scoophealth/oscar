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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page
	import="oscar.oscarEncounter.data.*,oscar.oscarEncounter.pageUtil.EctSessionBean, java.net.*"%>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<link rel="stylesheet" type="text/css" media="print" href="print.css" />
<link rel="stylesheet" type="text/css" href="encounterPrintStyles.css" />
<%
    //The oscarEncounter session manager, if the session bean is not in the context it looks for a session cookie with the appropriate name and value, if the required cookie is not available
    //it dumps you out to an erros page.

  EctSessionBean bean = new EctSessionBean();
  if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
    response.sendRedirect("error.jsp");
    return;
  }
  bean.setUpEncounterPage(LoggedInInfo.getLoggedInInfoFromSession(request), request.getParameter("echartid"),request.getParameter("demographic_no"));
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
        return true;
    }
    function onClose() {
        window.close();
        return true;
    }
</script>
<html:html>
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors />

<table class="Header">
	<tr>
		<td align="left"><input type="button" value="Print"
			onclick="javascript:return onPrint();" /> <input type="button"
			value="Close" onclick="javascript:return onClose();" /></td>
	</tr>
</table>

<table cellpadding="0" cellspacing="0"
	style="border-collapse: collapse; width: 7in; padding-left: 3px;">
	<tr>
		<td style="text-align: left; height: 34px;"><span
			style="font-weight: bold;"><%=bean.patientLastName %>, <%=bean.patientFirstName%>
		<%=bean.patientSex%> <%=bean.patientAge%></span></td>
		<td style="text-align: right; height: 34px;"><span
			style="font-weight: bold;">Dr. <%=providerBean.getProperty(bean.familyDoctorNo)%></span>
		</td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0"
	style="border-collapse: collapse; width: 7in; padding-left: 3px;">
	<tr>
		<td colspan="2"
			style="border-left: 2px solid #A9A9A9; border-right: 2px solid #A9A9A9; border-bottom: 2px solid #A9A9A9;"
			valign="top">
		<table width="100%">
			<tr>
				<td>
				<table width="100%">
					<tr>
						<td width="33%">
						<div class="RowTop">Social History:</div>
						</td>
						<td width="33%">
						<div class="RowTop">Family History:</div>
						</td>
						<td width="33%">
						<div class="RowTop">Medical History:</div>
						</td>
					</tr>
					<tr>
						<td valign="top" align="left" class="TableWithBorder"><pre
							name='shTextarea' style="font-size: 8pt;"><%=bean.socialHistory%>&nbsp;</pre>
						</td>
						<td valign="top" class="TableWithBorder"><pre
							name='fhTextarea' style="font-size: 8pt;"><%=bean.familyHistory%>&nbsp;</pre>
						</td>
						<td valign="top" class="TableWithBorder"><pre
							name='mhTextarea' style="font-size: 8pt;"><%=bean.medicalHistory%>&nbsp;</pre>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<table width="100%">
					<tr>
						<td width="50%">
						<div class="RowTop">Ongoing Concerns:</div>
						</td>

						<td width="50%">
						<div class="RowTop">Reminders:</div>
						</td>
					</tr>
					<tr width="100%">
						<td valign="top" class="TableWithBorder"><pre
							name='ocTextarea' style="font-size: 8pt;"><%=bean.ongoingConcerns%>&nbsp;</pre>
						</td>
						<td valign="top" class="TableWithBorder"><pre
							name='reTextarea' style="font-size: 8pt;"><%=bean.reminders%>&nbsp;</pre>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<!--encounter row-->
			<tr>
				<td>
				<table width="100%">
					<tr>
						<td width=100%>
						<div class="RowTop">Encounter:</div>
						</td>
					</tr>
					<tr>
						<td class="TableWithBorder" valign="top" style="text-align: left"
							width=100%><pre name='enTextarea' style="font-size: 8pt;"><%=bean.encounter%></pre>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<!----End new rows here-->
		</table>
		</td>
	</tr>
</table>

<table class="Header">
	<tr>
		<td align="left"><input type="button" value="Print"
			onclick="javascript:return onPrint();" /> <input type="button"
			value="Close" onclick="javascript:return onClose();" /></td>
	</tr>
</table>

</body>
</html:html>
