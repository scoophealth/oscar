<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->
<%@ include file="/taglibs.jsp"%>
<div id="banner">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td align="left">
		<%if (oscar.OscarProperties.getInstance().isTorontoRFQ()){%>
 	        <img src="<html:rewrite page="/images/QuatroShelterLogo.gif"  />" alt="QuatroShelter" id="caisilogo"  border="0"/>
 	    <%} else {%>
	        <img src="<html:rewrite page="/images/caisi_1.jpg" />" alt="Caisi" id="caisilogo"  border="0"/>
	    <%}%>
        </td>
        <td width="100%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome <b><c:out value="${sessionScope.provider.formattedName}" /></b>
        </td>
        <td align="right"><html:link action="/PMmodule/ProviderInfo.do">Home</html:link></td>
        <td>&nbsp;</td>
        <td>&nbsp;
               <a target="_blank" href='<%=request.getContextPath()%>/help/index.heml'>Help</a>
        &nbsp;</td>
        <td align="right">&nbsp;
               <a href='<%=request.getContextPath()%>/logout.jsp'>Logout</a>
        </td>
	</tr>
</table>
</div>
