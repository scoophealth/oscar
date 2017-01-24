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

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
String demographic_no = request.getParameter("demo");
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(Integer.parseInt(demographic_no));
pageContext.setAttribute( "demoExt", demoExt );
%>
<ul>
<!-- <li><strong>First Nations (INAC)</strong></li> -->

<li> 
	<span class="label">Band Number:</span> 
	<span class="info">${ demoExt["statusNum"] }</span>
</li>

<oscar:oscarPropertiesCheck value="false" defaultVal="true" property="showBandNumberOnly">
	<li>
		<span class="label">Band Name: </span> 
		<span class="info">${ demoExt["fNationCom"] }</span>
	</li>
	
	<li>
		<span class="label">Family Number: </span> 
		<span class="info">${ demoExt["fNationFamilyNumber"] }</span>
	</li>
	
	<li>
		<span class="label">Family Position: </span> 
		<span class="info">${ demoExt["fNationFamilyPosition"] }</span>
	</li>


	<li>
		<span class="label">Status: </span>
		 
		<span class="info">
			<select name="ethnicityDisplay" id="ethnicityDisplay" disabled="disabled" >
				<option value="-1" ${ demoExt['ethnicity'] eq -1 ? 'selected' : '' } >Not Set</option>
				<option value="1" ${ demoExt['ethnicity'] eq 1 ? 'selected' : '' } >On-reserve</option>
				<option value="2" ${ demoExt['ethnicity'] eq 2 ? 'selected' : '' } >Off-reserve</option>
				<option value="3" ${ demoExt['ethnicity'] eq 3 ? 'selected' : '' } >Non-status On-reserve</option>
				<option value="4" ${ demoExt['ethnicity'] eq 4 ? 'selected' : '' } >Non-status Off-reserve</option>
				<option value="5" ${ demoExt['ethnicity'] eq 5 ? 'selected' : '' } >Metis</option>
				<option value="6" ${ demoExt['ethnicity'] eq 6 ? 'selected' : '' } >Inuit</option>
				<option value="11" ${ demoExt['ethnicity'] eq 11 ? 'selected' : '' } >Homeless</option>
				<option value="12" ${ demoExt['ethnicity'] eq 12 ? 'selected' : '' } >Out of Country Residents</option>
				<option value="13" ${ demoExt['ethnicity'] eq 13 ? 'selected' : '' } >Other</option>
			</select> 
		</span>
	</li>
</oscar:oscarPropertiesCheck>

</ul>
