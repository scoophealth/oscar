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
<%@page import="org.oscarehr.PMmodule.wlservice.ProgramQuery"%>
<%@page import="org.oscarehr.PMmodule.wlservice.VacancyQuery"%>
<%@page import="org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO"%>
<%@page import="org.oscarehr.PMmodule.wlmatch.MatchBO"%>
<%@page import="org.oscarehr.PMmodule.wlservice.TopMatchesQuery"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="javax.xml.datatype.DatatypeFactory"%>
<%@page import="org.oscarehr.PMmodule.wlservice.MatchParam"%>
<%@page import="org.oscarehr.PMmodule.wlservice.WaitListService"%>
<%@ include file="/taglibs.jsp"%>
<%
	WaitListService s = new WaitListService();
	List<VacancyDisplayBO> listVac = s.listVacanciesForAllWaitListPrograms();
%>

<% 
request.setAttribute("listVac", listVac);
%>

<display:table class="simple" id="vacancyList" name="requestScope.listVac" 
 pagesize="1000"  requestURI="/PMmodule/AllVacancies.do" >
    <display:column property="vacancyID" title="Vacancy Id" sortable="true" url="/PMmodule/VacancyClientMatch.do" paramId="vacancyId" paramProperty="vacancyID" />
    <display:column property="programName" title="Program Name" sortable="true" url="/PMmodule/ProgramManagerView.do" paramId="id" paramProperty="programId" />
    <display:column property="vacancyTemplateName" title="Vacancy Template" sortable="true" url="/PMmodule/VacancyClientMatch.do" paramId="vacancyId" paramProperty="vacancyID" />
    <display:column property="vacancyName" title="Vacancy Name" sortable="true"/>
    <display:column property="created" title="Created date" sortable="true"/>
    <display:column property="pendingCount" title="Forwarded" sortable="true"/>
    <display:column property="rejectedCount" title="Rejected" sortable="true"/>
    <display:column property="acceptedCount" title="Accepted" sortable="true"/>
  </display:table>


