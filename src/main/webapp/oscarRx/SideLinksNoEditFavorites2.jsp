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
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
        oscar.oscarRx.pageUtil.RxSessionBean bean2 = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        org.oscarehr.common.model.Allergy[] allergies = RxPatientData.getPatient(LoggedInInfo.getLoggedInInfoFromSession(request), bean2.getDemographicNo()).getActiveAllergies();
        String alle = "";
        if (allergies.length > 0 ){ alle = "Red"; }

        %>


<td width="10%" height="100%" valign="top">
<div class="PropSheetMenu">

<security:oscarSec roleName="<%=roleName$%>" objectName="_allergy" rights="r" reverse="<%=false%>">

<p class="PropSheetLevel1CurrentItem<%=alle%>"><bean:message key="oscarRx.sideLinks.msgAllergies"/></p>
<p class="PropSheetMenuItemLevel1">
<%for (int j=0; j<allergies.length; j++){%>

<p class="PropSheetMenuItemLevel1"><a
	title="<%= allergies[j].getDescription() %> - <%= allergies[j].getReaction() %>">
<%=allergies[j].getShortDesc(13,8,"...")%> </a></p>
<%}%>
</p>

</security:oscarSec>

<p class="PropSheetLevel1CurrentItem"><bean:message key="oscarRx.sideLinks.msgFavorites"/></p>
<p class="PropSheetMenuItemLevel1">
<%
        oscar.oscarRx.data.RxPrescriptionData.Favorite[] favorites = new oscar.oscarRx.data.RxPrescriptionData().getFavorites(bean2.getProviderNo());
        for (int j=0; j<favorites.length; j++){ %>

<p class="PropSheetMenuItemLevel1"><a href="javascript:void(0);"
	onclick="goSD3('<%= favorites[j].getFavoriteId() %>');"
	title="<%= favorites[j].getFavoriteName() %>"> <%if(favorites[j].getFavoriteName().length()>13){ %>
<%= favorites[j].getFavoriteName().substring(0, 10) + "..." %> <%} else {%>
<%= favorites[j].getFavoriteName() %> <%}%> </a></p>
<%}%>
</p>
</div>
</td>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript">

    function goSD3(favoriteId){
        location.href="SearchDrug3.jsp?usefav=true&favid="+favoriteId;
    }

</script>
