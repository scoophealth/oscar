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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="oscar.oscarEncounter.immunization.data.*"%>
<%@ page
	import="oscar.oscarEncounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null)
    {response.sendRedirect("error.jsp");
    return;}
    oscar.oscarEncounter.data.EctRemoteAttachments remoAttach = new oscar.oscarEncounter.data.EctRemoteAttachments();
    remoAttach.estMessageIds(bean.getDemographicNo());
    out.print(bean.getDemographicNo());
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.RemoteAttachments.title" /></title>
<script type="text/javascript" language=javascript>
function popupViewAttach(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.RemoteAttachments.msgViewAtt"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function popupSendAttach(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.RemoteAttachments.msgSendAtt"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
</script>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1"
	name="<bean:message key="oscarEncounter.RemoteAttachments.msgEncounterTable"/>">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarEncounter.RemoteAttachments.title" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><%= bean.patientLastName %> , <%= bean.patientFirstName%>
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="attachment" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">
		<%String prov = bean.getDemographicNo();
              String demog = bean.providerNo;
            %> <a
			href="javascript:popupSendAttach(700,960,'../oscarMessenger/Transfer/SelectItems.jsp?val1=<%=demog%>&val2=<%=prov%>')"><bean:message
			key="oscarEncounter.RemoteAttachments.msgSendEDoc" /></a></td>
		<td class="MainTableRightColumn">
		<h2><bean:message
			key="oscarEncounter.RemoteAttachments.msgDemogAtt" /></h2>
		<table border="0" width="80%" cellspacing="1">
			<tr>
				<th bgcolor="#DDDDFF"><bean:message
					key="oscarEncounter.RemoteAttachments.msgSubject" /></th>
				<th bgcolor="#DDDDFF"><bean:message
					key="oscarEncounter.RemoteAttachments.msgSentFrom" /></th>
				<th bgcolor="#DDDDFF"><bean:message
					key="oscarEncounter.RemoteAttachments.msgSavedBy" /></th>
				<th bgcolor="#DDDDFF"><bean:message
					key="oscarEncounter.RemoteAttachments.msgDate" /></th>

			</tr>
			<%
                for(int i=0; i < remoAttach.messageIds.size(); i++){
                String mesId   = (String) remoAttach.messageIds.get(i);
                String theDate = (String) remoAttach.dates.get(i);
                String svBy    = (String) remoAttach.savedBys.get(i);

                java.util.ArrayList lis = remoAttach.getFromLocation(mesId);

                String fromLoco = (String) lis.get(0);
                String subject  = (String) lis.get(1);
                %>
			<tr>
				<td bgcolor="#EEEEFF"><a
					href="javascript:popupViewAttach(700,960,'ViewAttachment.do?mesId=<%=mesId%>')"><%=subject%></a></td>
				<td bgcolor="#EEEEFF"><%=fromLoco%></td>
				<td bgcolor="#EEEEFF"><%=svBy%></td>
				<td bgcolor="#EEEEFF"><%=theDate%></td>

			</tr>

			<%}%>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
