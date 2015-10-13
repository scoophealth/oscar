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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicArchiveDao" %>
<%@page import="org.oscarehr.common.model.DemographicArchive" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>
<%@page import="oscar.util.DateUtils" %>
<%@page import="oscar.util.StringUtils" %>
<%@page import="oscar.oscarDemographic.pageUtil.Util" %>
<html:html locale="true">
<head>
<title>Enrollment History</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="styles.css">
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<%
	String demographicNo = request.getParameter("demographicNo");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	
	//load demographic
	DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
	Demographic demographic = demographicDao.getClientByDemographicNo(Integer.valueOf(demographicNo));

	//load current roster status
	String rosterStatus = demographic.getRosterStatus();
	Date rosterDate = demographic.getRosterDate();
	Date rosterTermDate = demographic.getRosterTerminationDate();
%>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%" height="100%" valign="top"><!--Column Two Row Two-->
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<!----Start new rows here-->
			<tr>
				<td align="right"><span><input type="button"
					onclick="window.print();" value="Print" class="printCell"></span>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead">Patient Information</div>
				</td>
			</tr>
			<tr>
				<td><b>Name</b>: <%=demographic.getFormattedName() %></td>
			</tr>
			<tr>
				<td><b>DOB</b>: <%=demographic.getFormattedDob() %></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead">Patient Enrollment History</div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td width="100%"><!--<div class="Step1Text" style="width:100%">-->
						<table width="100%" cellpadding="3">
							<tr>
								<th align="left" width="50%" nowrap="nowrap"><b>Date Changed</b></th>
								<th align="left" width="50%" nowrap="nowrap"><b>Roster Status</b></th>
								<th align="left" width="50%" nowrap="nowrap"><b>Date</b></th>
								<th align="left" width="50%" nowrap="nowrap"><b>Rostered To</b></th>
								<th align="left" width="50%" nowrap="nowrap"><b>Updated By</b></th>
								<th align="left" width="50%" nowrap="nowrap"></th>											
							</tr>
							
							<tr>
								<td nowrap="nowrap"><%=DateUtils.formatDate(demographic.getLastUpdateDate(),request.getLocale())%></td>
								<td nowrap="nowrap"><%=viewRS(rosterStatus)%></td>
								
							<%Provider demoP = providerDao.getProvider(demographic.getProviderNo());
							   if("RO".equals(rosterStatus)){ %>
								<td nowrap="nowrap"><%=DateUtils.formatDate(rosterDate,request.getLocale())%></td>
							<%}else if(StringUtils.filled(rosterStatus)){ %>
								<td nowrap="nowrap"><%=DateUtils.formatDate(rosterTermDate,request.getLocale())%></td>
							<%}else{ %>
								<td nowrap="nowrap"></td>
								<td nowrap="nowrap"><%=(demoP!=null && "RO".equals(demographic.getRosterStatus()))?demoP.getFormattedName():"" %></td>
							<%}
							   if(StringUtils.filled(demographic.getLastUpdateUser())){ %>
								<td nowrap="nowrap"><%=providerDao.getProvider(demographic.getLastUpdateUser()).getFormattedName() %></td>
							<%}else{ %>
								<td nowrap="nowrap">System</td>
							<%} %>
							</tr>
						<%if(!"RO".equals(rosterStatus)
								&& demographic.getRosterTerminationReason()!=null
								&& !demographic.getRosterTerminationReason().equals("")){ %>
							<tr>
								<td nowrap="nowrap">Termination Reason: </td>
								<td colspan="5"><%=Util.rosterTermReasonProperties.getReasonByCode(demographic.getRosterTerminationReason()) %></td>
							</tr>
						<%} %>
						<%
								DemographicArchiveDao demoArchiveDao = (DemographicArchiveDao)SpringUtils.getBean("demographicArchiveDao");
								List<DemographicArchive> DAs = demoArchiveDao.findRosterStatusHistoryByDemographicNo(Integer.valueOf(demographicNo));
								for(int i=0;i<DAs.size();i++) {
									DemographicArchive da = DAs.get(i);
					                String historyRS = StringUtils.noNull(da.getRosterStatus());
					                
					                if (i==0) { //check history info with current
						                Date rd = da.getRosterDate();
						                Date td = da.getRosterTerminationDate();
					                	if (historyRS.equals(rosterStatus) &&
				                			DateUtils.nullSafeCompare(td, rosterDate).equals(0) &&
				                			DateUtils.nullSafeCompare(td, rosterTermDate).equals(0))
					                		continue;
					                }
					                %>
					                	<tr>
					                		<td nowrap="nowrap"><%=DateUtils.formatDate(da.getLastUpdateDate(),request.getLocale())%></td>
					                		<td nowrap="nowrap"><%=viewRS(historyRS)%></td>
				                		<%if("RO".equals(historyRS)){ %>
											<td nowrap="nowrap"><%=DateUtils.formatDate(da.getRosterDate(),request.getLocale())%></td>
										<%}else if( historyRS != null && !historyRS.trim().equals("")){ %>
											<td nowrap="nowrap"><%=DateUtils.formatDate(da.getRosterTerminationDate(),request.getLocale())%></td>
										<%}%>
					                		<td nowrap="nowrap">
					                		<%
					                		String name = "";
					                		if(StringUtils.filled(da.getProviderNo()) && "RO".equals(historyRS)) {
					                			Provider p  = providerDao.getProvider(da.getProviderNo());
					                			if(p != null) {
					                				name = p.getFormattedName();
					                			}
					                		}
					                		%>
					                		<%=name %>
					                		</td>
				                		<%if(StringUtils.filled(da.getLastUpdateUser())){ %>
				                		    <td nowrap="nowrap">
				                		    	<%
				                		    	name = "";
				                		    	Provider p = providerDao.getProvider(da.getLastUpdateUser());
				                		    	if(p != null) {
				                		    		name = p.getFormattedName();
				                		    	}
				                		    	%>
				                		    	<%=name %>
				                		    </td>
				                		<%}else{ %>
				                		    <td nowrap="nowrap">System</td>
				                		<%}%>
					                	</tr>
									<%if(!"RO".equals(da.getRosterStatus())
											&& da.getRosterTerminationReason()!=null
											&& !da.getRosterTerminationReason().equals("")){ %>
										<tr>
											<td nowrap="nowrap">Termination Reason: </td>
											<td colspan="5"><%=Util.rosterTermReasonProperties.getReasonByCode(da.getRosterTerminationReason()) %></td>
										</tr>
									<%}
								}
								
								
								
							%>
						</table>

						</div>
						<div style="margin-top: 10px; margin-left: 20px; width: 100%">
						<table width="100%" cellspacing=0 cellpadding=0>
							<tr>
								
							</tr>
						</table>
						<!--</div>-->
						</td>
					</tr>
				</table>
				</td>
			</tr>





			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>





</table>





</body>
</html:html>

<%! public String viewRS(String code)  {
	if(StringUtils.empty(code)) {
		return "&lt;Not Set&gt;";
	}
	if(("RO").equals(code)) {
		return "Rostered";
	}
	if(("NR").equals(code)) {
		return "Not Rostered";
	}
	if(("TE").equals(code)) {
		return "Terminated";
	}
	if(("FS").equals(code)) {
		return "Fee for Service";
	}
	return code;
}
%>
