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

<%@page language="java" import="java.util.*"%>
<%@page import="org.oscarehr.PMmodule.wlmatch.MatchBO"%>
<%@ include file="/taglibs.jsp"%>



	<table height="100%" width="100%" border="1" class="simple" cellpadding="3" cellspacing="2"  >
    <tr>
    <td height="100%" width="30%">
    		<table border="0" rules="rows" width="100%" height="50%"  >
         		<thead>
         		<tr width="100%">
          			<th>Vacancy</th>
         		</tr>
         		</thead>
         		<tr width="100%">
           			<td> <bean:write name="vacancyClientMatchForm" property="vacancy"/> </td>
         		</tr>
        	</table>
        	<table border="0" rules="rows" width="100%" height="50%"  >
         		<thead>
         		<tr >
          			<th> Vacancy Template </th>
         		</tr>
         		</thead>
         		<tr>
            		<td> <bean:write name="vacancyClientMatchForm" property="template"/> </td>
         		</tr>
        	</table>
    </td>
    
    
		
    <td height="100%" width="70%">
    	<table border="1" rules="rows" width="100%" >
    		<thead>
         		<tr >
          			<th> Criteria </th>
         		</tr>
         		</thead>
    		<logic:iterate id="criteria" property="criteriaList" name="vacancyClientMatchForm" type="String">
				<tr>
					<td><c:out value="${criteria}" /></td>
				</tr>			
		</logic:iterate>
    	</table>
    </td>
    
    </tr>
    </table>
    	
	
	<br/>
	<%
		String vacancyId = request.getParameter("vacancyId");
		
		int i = 0;
    %>
    <table cellpadding="3" cellspacing="2">
    <tr>
    <td>
    <table class="simple" cellpadding="3" cellspacing="2" width="100%">
    <thead>
		<tr>
			<th>Client No</th>
			<th>Name</th>
			<th>Days in WL</th>
			<th>Days since last contact</th>
			<th>Intake Form</th>
			<th>Create Referral<br/>(Forward Match)</th>
			<th>Match %</th>
		</tr>
		</thead>
		<tbody>
		<logic:iterate id="matchedCLient" property="clientList" name="vacancyClientMatchForm" type="org.oscarehr.PMmodule.wlmatch.MatchBO">
		<%
		i++;
		if (i % 2 == 0) {
		%>
		<tr class="even">
			<%
				} else {
			%>
		
		<tr class="odd">
			<%
				}
			%>
			<td>
				<a 
				href="<html:rewrite action="/PMmodule/ClientManager.do"/>?id=<c:out value="${matchedCLient.clientID}"/>">
					<c:out value="${matchedCLient.clientID}" /></a>
			</td>
			<td>
				<a 
				href="<html:rewrite action="/PMmodule/ClientManager.do"/>?id=<c:out value="${matchedCLient.clientID}"/>">
					<c:out value="${matchedCLient.clientName}" /></a>
			</td>
			<td><c:out value="${matchedCLient.daysInWaitList}"></c:out></td>
			<td><%=matchedCLient.getDaysSinceLastContact()>-1 ? (""+matchedCLient.getDaysSinceLastContact()) : "" %></td>
			<td>
				<a href="../eform/efmshowform_data.jsp?fdid=<c:out value="${matchedCLient.formDataID}"/>">
					Form</a>			
			</td>
			<td>				<a 
				href="<html:rewrite action="/PMmodule/ClientManager.do"/>?method=vacancy_refer_select_program&program.id=<bean:write name="vacancyClientMatchForm" property="programId"/>&vacancyId=<%=vacancyId %>&vacancyName=<bean:write name="vacancyClientMatchForm" property="vacancy"/>&view.tab=Refer&id=<c:out value="${matchedCLient.clientID}" />">
					Create Referral</a>
			</td>
			<td><c:out value="${matchedCLient.percentageMatch}"></c:out></td>
		</tr>
		</logic:iterate>
		</tbody>
	</table>
    </td>
    </tr>
    </table>
	
