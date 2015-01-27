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
	request.setAttribute("providers",providerDao.getActiveProvidersByType("doctor"));
%>

<div id="followup_<%=id%>">
					<input type="hidden" name="followup_<%=id%>.id" id="followup_<%=id%>.id" value=""/>
					
					<a href="#" onclick="deleteFollowUp(<%=id%>);">[Delete]</a>
					
					&nbsp;
					
		            <select name="followup_<%=id%>.type" id="followup_<%=id%>.type">
		            	<option value="followup">Follow up</option>
		            	<option value="consult">Consult</option>
		            </select>
	            	
	            	&nbsp;
	            		             
		            <select name="followup_<%=id%>.followupProvider" id="followup_<%=id%>.followupProvider">
		            	<c:forEach var="item" items="${providers}">
		            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
		            	</c:forEach>            	
					</select>
				
	            	&nbsp;
	            			           
		             <input type="text" name="followup_<%=id%>.timespan" id="followup_<%=id%>.timespan" size="4" maxlength="6"/>		             
		             <select name="followup_<%=id%>.timeframe" id="followup_<%=id%>.timeframe" >
		             	<option value="days">days</option>
		            	<option value="weeks">weeks</option>
		            	<option value="months">months</option>
		            </select>
	            	            	
					&nbsp;
					
		            <select name="followup_<%=id%>.urgency" id="followup_<%=id%>.urgency">
		             	<option value="routine">routine</option>
		            	<option value="ASAP">ASAP</option>            	
		            </select>
	           
	           		&nbsp;
	           		Comment:
	           		<input type="text" name="followup_<%=id%>.comment" id="followup_<%=id%>.comment" size="40"/>		
</div>
