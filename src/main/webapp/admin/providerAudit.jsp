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

<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.common.model.OscarLog"%>
<%@page import="org.oscarehr.common.dao.OscarLogDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager, org.oscarehr.caisi_integrator.ws.CachedAppointment, org.oscarehr.caisi_integrator.ws.CachedProvider, org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.caisi_integrator.ws.*"%>
<%@ page import="org.oscarehr.common.model.CachedAppointmentComparator" %>

<%@page import="oscar.util.DateUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>

<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.model.AppointmentArchive" %>
<%@page import="org.oscarehr.common.dao.AppointmentStatusDao" %>
<%@page import="org.oscarehr.common.model.AppointmentStatus" %>


<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script> 
   <script>
     jQuery.noConflict();
   </script>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script>
	var ctx = '<%=request.getContextPath()%>';
</script>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);
	
	Provider provider = providerDao.getProvider(request.getParameter("provider_no"));
	
	List<OscarLog> logs = oscarLogDao.findByProviderNo(provider.getId());
	
%>

<title>Audit for </title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<script type="text/javascript">

	function popupPageNew(vheight,vwidth,varpage) {
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
	  var popup=window.open(page, "demographicprofile", windowprops);
	  if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
	  }
	}

	function printVisit() {
		printVisit('');               
	}
	
	
	jQuery(document).ready(function(){
		
	});
	
</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Audit</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Audit Log Information for User : <%=provider.getFormattedName()%>(<%=provider.getId()%>)</td>
				<td>&nbsp;</td>
				<td style="text-align: right" <a href="javascript:popupStart(300,400,'../About.jsp')">
					<bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'../License.jsp')">
					<bean:message key="global.license" /></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
			 
	    </td>
		<td class="MainTableRightColumn">
			<table style="width:100%">
				<thead>
					<th align="left">Time of Event</th>
					<th align="left">Demographic</th>
					<th align="left">Action</th>
					<th align="left">Content</th>
					<th align="left">Content ID</th>
				</thead>
				<tbody>
					<%
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					int index = 0;
					for(OscarLog log : logs) {
						
						if(log.getContent() == null && log.getContentId() == null) {
							continue;
						}
						
						Demographic demographic = demographicDao.getDemographicById(log.getDemographicId());
					%>
					<tr bgcolor="<%=(index%2==0)?"ivory":"white"%>">
						<td><%=fmt.format(log.getCreated()) %></td>
						<td><%=demographic != null ? demographic.getFormattedName() : ""%></td>
						<td><%=log.getAction() %></td>
						<td><%=log.getContent() %></td>
						<td><%=log.getContentId() != null  && !"null".equals(log.getContentId())? log.getContentId() : "" %></td>
						
						
					</tr>
					<% index++;
					} %>
				</tbody>
			</table>
		</td>
	</tr>
</table>
</body>
</html:html>
