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

<%@ page language="java" import="java.util.*"%>
<%@page import="org.caisi.wl.ProgramQuery"%>
<%@page import="org.caisi.wl.VacancyQuery"%>
<%@page import="org.caisi.wl.VacancyDisplayBO"%>
<%@page import="org.caisi.wl.MatchBO"%>
<%@page import="org.caisi.wl.TopMatchesQuery"%>
<%@page import="org.caisi.wl.WaitListService_Service"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="javax.xml.datatype.DatatypeFactory"%>
<%@page import="org.caisi.wl.MatchParam"%>
<%@page import="org.caisi.wl.WaitListService"%>
<% 
WaitListService s=new WaitListService_Service().getWaitListServicePort(); 

ProgramQuery q=new ProgramQuery();
q.setProgramID(10018);
List<VacancyDisplayBO> list=s.listVacanciesForWaitListProgram(q);

%><table>
	<tr><td>Vacancy ID</td><td>Vacancy Template</td><td>Created date</td><td>Forwarded</td><td>Rejected</td><td>Accepted</td></tr>
<%for(VacancyDisplayBO m:list){
	String link="?vacancyID=" + m.getVacancyID();%>
	<tr><td><a href="<%=link %>"><%=m.getVacancyID() %></a></td><td><a href="<%=link %>"><%=m.getVacancyTemplateName() %></a></td><td><%=m.getCreated() %></td><td><%=m.getPendingCount() %></td><td><%=m.getRejectedCount() %></td><td><%=m.getAcceptedCount() %></td></tr>
<%}%>
</table>