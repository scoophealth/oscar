
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


<%@ include file="/taglibs.jsp"%>
<div id="banner">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td align="left" width="200px">
		<%if (oscar.OscarProperties.getInstance().isTorontoRFQ() && !oscar.OscarProperties.getInstance().getBooleanProperty("USE_CAISI_LOGO", "true")){%>
		<img src="<%=request.getContextPath()%>/images/QuatroShelterLogo.gif"
			alt="QuatroShelter" id="caisilogo" border="0" /> <%} else {%> <img
			src="<%=request.getContextPath()%>/images/caisi_1.jpg" alt="Caisi"
			id="caisilogo" border="0" /> <%}%>
		</td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome <b><c:out
			value="${sessionScope.provider.formattedName}" /></b>, current facility=<c:out
			value="${sessionScope.currentFacility.name}" /></td>
		<td align="right" width="60px"><html:link
			action="/PMmodule/ProviderInfo.do">Home</html:link></td>
		<!--  
        <td width="60px" align="center">
               <a target="_blank" href='<%=request.getContextPath()%>/help/index.html'>Help</a>
        </td>
        -->
		<td align="left" width="60px"><a
			href='<%=request.getContextPath()%>/logout.jsp'>Logout</a></td>
	</tr>
</table>
</div>
