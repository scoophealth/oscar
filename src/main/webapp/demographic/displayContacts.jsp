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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List, org.apache.commons.lang.StringUtils" %>
<%@ page import="org.oscarehr.common.web.ContactAction" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.DemographicContact" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.managers.DemographicManager" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<security:oscarSec roleName="${ sessionScope.userrole }" objectName="_demographic" rights="r" reverse="${ false }">

<% 
	List<DemographicContact> demographicContacts = null;
	Demographic demographic = null;
	String demographicNoString = request.getParameter("demographicNo");
	DemographicManager demographicManager = null;
	String type = request.getParameter("type");
	if(type == null) {
		type = DemographicContact.CATEGORY_PERSONAL;
	}
	if ( ! StringUtils.isBlank( demographicNoString ) ) {		
		demographicManager = SpringUtils.getBean(DemographicManager.class);
		demographicContacts = demographicManager.getHealthCareTeam(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.parseInt(demographicNoString),type);
		demographic = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNoString);
	}
	
	pageContext.setAttribute("demographic", demographic);
	pageContext.setAttribute("demographicContacts", demographicContacts);
%>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }">
	
	<!DOCTYPE html>
	<html>
	<head>
	
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/healthCareTeam.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/share/css/OscarStandardLayout.css" />
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.js" ></script>

</c:if>
<%-- END DETACHED VIEW ENABLED  --%> 

<c:if test="${ param.view ne 'detached' }" >
	<script type="text/javascript" >
		jQuery(document).ready( function($) {		
			//--> Popup effects
			jQuery(".hovereffect1").bind( "mouseover", function(){
				nhpup.popup( jQuery('#<%=type%>TeamMemberDetail_' + this.id).html(), { 'width':250 } );			
			});
		})
	</script>
</c:if>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }" >

	<script type="text/javascript">
		jQuery(document).ready( function($) {		
			//--> Popup effects
			$(".hovereffect1").mouseover(function(){
				$('#<%=type%>TeamMemberDetail_' + this.id).toggle();
				$(this).css("fontWeight", "bold");
			});
			$(".hovereffect1").mouseout(function(){
				$('#<%=type%>TeamMemberDetail_' + this.id).toggle();
				$(this).css("fontWeight", "inherit");
			});
		})
	</script>

	</head>
	
<body id="${ param.view }View" >
	<table class="MainTable" >
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="20%">Health Care Team</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>&nbsp;</td>
				<td>
					<c:out value="${ demographic.lastName }" />,&nbsp;
					<c:out value="${ demographic.firstName }" />&nbsp;
					<c:out value="${ demographic.age }" />&nbsp;years
				</td>
				<td style="text-align: right">
					<oscar:help keywords="contact" key="app.top1"/> | 
					<a href="javascript:popupStart(300,400,'About.jsp')">
					<bean:message key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')">
					<bean:message key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr><td colspan="2">
		
</c:if>
<%-- END DETACHED VIEW ENABLED  --%>


<%-- HEALTH CARE TEAM MODULE --%>
<div class="demographicSection" id="healthCareTeam">

<%-- DETACHED VIEW ENABLED  --%>

<%
	String header = null;
	if(type.equals( DemographicContact.CATEGORY_PERSONAL)) {
		header = "Personal Contacts";
	} else if(type.equals( DemographicContact.CATEGORY_OTHER)) {
		header = "Other Contacts";
	}

%>

	<h3 id="tableTitle"><%=header %></h3>
	
<%-- END DETACHED VIEW ENABLED  --%>

	<ul>
		<c:forEach items="${ demographicContacts }" var="dContact" varStatus="row">

			<c:set value="internal" var="internal" scope="page" />
			<c:set value="${ dContact.details.workPhone }" var="workPhone" scope="page" />
			<c:set value="even" var="rowclass" scope="page" />
			<c:if test="${ row.index mod 2 ne 0 }" >
				<c:set value="odd" var="rowclass" scope="page" />
			</c:if>
			
			<li id="${ dContact.id }" class="hovereffect1 ${ rowclass }" >
			
				<span class="label"> 
					<c:out value="${ dContact.role }" />					
				</span>
				
				<c:if test="${ workPhone eq internal }" > 
					<span class="label">
						&#40;<c:out value="${ internal }" />&#41;
					</span>
				</c:if>	
				
				<span class="info"> 
					:&nbsp;<c:out value="${ dContact.contactName }" />
				</span>
			</li>
			
			<table class="healthCareTeamMemberDetailTable" id="<%=type%>TeamMemberDetail_${ dContact.id }" style="display:none;" >
				<tr><th class="alignLeft contactName" colspan="2"><c:out value="${ dContact.contactName }" /></th></tr>
				<tr>
					<td class="alignRight alignTop smallText role" >Role:</td>
					<td class="alignLeft alignTop smallText role" ><c:out value="${ dContact.role }" /></td>
				</tr>
				<tr>
					<td class="alignRight alignTop smallText">Address:</td>
					<td class="alignLeft alignTop smallText"><c:out value="${ dContact.details.address }" /></td>
				</tr>
				<tr>
					<td class="alignRight alignTop smallText">City:</td>
					<td class="alignLeft alignTop smallText"><c:out value="${ dContact.details.city }" /></td>
				</tr>
				<tr>
					<td class="alignRight alignTop smallText">Province:</td>
					<td class="alignLeft alignTop smallText"><c:out value="${ dContact.details.province }" /></td>
				</tr>
				<tr>
					<td class="alignRight alignTop smallText">Phone: </td>
					<td class="alignLeft alignTop smallText"><c:out value="${ not empty workPhone ? workPhone : unknown }" /></td>
				</tr>
				<tr>
					<td class="alignRight alignTop smallText">Fax: </td>
					<td class="alignLeft alignTop smallText"><c:out value="${ not empty dContact.details.fax ? dContact.details.fax : unknown }" /></td>
				</tr>
				
			</table>
			
		</c:forEach>
	</ul>
</div>
<%-- END HEALTH CARE TEAM MODULE --%>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }">
	</td></tr>
	</table>
	</body>
	</html>
</c:if>
<%-- END DETACHED VIEW ENABLED  --%>

</security:oscarSec>
