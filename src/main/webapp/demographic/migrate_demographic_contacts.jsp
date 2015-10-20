<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.model.DemographicContact" %>
<%@page import="org.oscarehr.common.model.Relationships" %>
<%@page import="org.oscarehr.common.dao.DemographicContactDao" %>
<%@page import="org.oscarehr.common.dao.RelationshipsDao" %>
<%@ include file="/taglibs.jsp"%>
<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	RelationshipsDao relationshipsDao = (RelationshipsDao)SpringUtils.getBean(RelationshipsDao.class);
	DemographicContactDao demographicContactDao = (DemographicContactDao)SpringUtils.getBean(DemographicContactDao.class);
%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<h1>Demographic Contacts - Migration</h1>

<script>
	var ctx = '<%=request.getContextPath()%>';
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
<script>

</script>

<form method="post" id="ocanForm" action="migrate_demographic_contacts.jsp">
	<input type="hidden" name="method" value="migrate"/>
	<table class="borderedTableAndCells" style="border:none">
		<tr>
			<td>Perform Migration</td>
			<td >
				<input type="submit" value="Go"/>
			</td>
		</tr>
	</table>
</form>


<%
	String method = request.getParameter("method");
	if(method != null && method.equals("migrate")) {
		List<Relationships> oldRelationships = relationshipsDao.findAll();
		for(Relationships r:oldRelationships) {
			DemographicContact dc = new DemographicContact();
			dc.setCreated(r.getCreationDate());
			dc.setDeleted(r.getDeleted().equals("1")?true:false);
			dc.setCategory(DemographicContact.CATEGORY_PERSONAL);
			dc.setContactId(new Integer(r.getRelationDemographicNo()).toString());
			dc.setCreator(r.getCreator());
			dc.setDemographicNo(r.getDemographicNo());
			dc.setEc(r.getEmergencyContact().equals("1")?"true":"false");
			dc.setFacilityId(r.getFacilityId());
			dc.setNote(r.getNotes());
			dc.setRole(r.getRelation());
			dc.setSdm(r.getSubDecisionMaker().equals("1")?"true":"false");
			dc.setType(DemographicContact.TYPE_DEMOGRAPHIC);

			if(demographicContactDao.find(dc.getDemographicNo(), Integer.parseInt(dc.getContactId())).size()==0) {
				demographicContactDao.persist(dc);
			}
		}

		%><Br/><h3>Migration Complete</h3><%
	}

%>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
