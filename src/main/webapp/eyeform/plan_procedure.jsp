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

<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
	String id = request.getParameter("id");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	request.setAttribute("providers",providerDao.getActiveProviders());
%>

<div id="procedure_<%=id%>">

				<input type="hidden" name="procedure_<%=id%>.id" value=""/>
					
				<a href="#" onclick="deleteProcedure(<%=id%>);">[Delete]</a>
				
				&nbsp;
		
				<select name="procedure_<%=id%>.eye">
					<option value="OU">OU</option>
					<option value="OD">OD</option>
					<option value="OS">OS</option>
					<option value="OD then OS">OD then OS</option>
					<option value="OS then OD">OS then OD</option>
				</select>
			
				&nbsp;
				Proc:
				<input type="text" name="procedure_<%=id%>.procedureName" size="30"/>			
				
				&nbsp;
				Loc:
				<input type="text" name="procedure_<%=id%>.location" size="5"/>
						
				&nbsp;
				
				
				<select name="procedure_<%=id%>.urgency">
					<option value="routine">routine</option>
					<option value="ASAP">ASAP</option>
					<option value="URGENT">URGENT</option>					
				</select>
				
				&nbsp;	
								
				Comment:	
				<input type="text" name="procedure_<%=id%>.comment" size="40"/>
				

</div>
