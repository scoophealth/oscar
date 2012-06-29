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
<%@page import="org.oscarehr.PMmodule.wlservice.VacancyQuery"%>
<%@page import="org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO"%>
<%@page import="org.oscarehr.PMmodule.wlmatch.MatchBO"%>
<%@page import="org.oscarehr.PMmodule.wlservice.TopMatchesQuery"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="javax.xml.datatype.DatatypeFactory"%>
<%@page import="org.oscarehr.PMmodule.wlservice.MatchParam"%>
<%@page import="org.oscarehr.PMmodule.wlservice.WaitListService"%>
<% 
WaitListService s=new WaitListService(); 

String act=request.getParameter("action");
try{ 
if(act!=null && act.length()>0){
	if(act.equals("recordContactAttempt")){
		MatchParam p=new MatchParam();
		p.setClientID(Integer.parseInt(request.getParameter("clientID")));
		p.setVacancyID(Integer.parseInt(request.getParameter("vacancyID")));
		s.recordContactAttempt(p);
	}else if(act.equals("recordContact")){
		MatchParam p=new MatchParam();
		p.setClientID(Integer.parseInt(request.getParameter("clientID")));
		p.setVacancyID(Integer.parseInt(request.getParameter("vacancyID")));
		p.setContactDateTime(new Date());
		s.recordClientContact(p);
	}
} 
}catch(Throwable t){
}
Integer vacancyID=null;
try{
vacancyID=Integer.parseInt(request.getParameter("vacancyID"));
}catch(Throwable t){}
if(vacancyID!=null){
	TopMatchesQuery q=new TopMatchesQuery();
	q.setMaximum(10);
	q.setVacancyID(vacancyID);
	
	List<MatchBO> list=s.listTopMatches(q);
	
	VacancyQuery vq=new VacancyQuery();
	vq.setVacancyID(vacancyID);
	VacancyDisplayBO dis=s.getVacancy(vq);
	
	%>
	Template: <%= dis.getVacancyTemplateName()%><br>
	Criteria: <%= dis.getCriteriaSummary()%><br>
	<hr>
	<table>
		<tr><td>Client #</td><td>Client name</td><td>Contact attempts</td><td>Days since last contact</td><td>% match</td><td>Form</td><td>Actions</td></tr>
	<%for(MatchBO m:list){%>
		<tr><td><%=m.getClientID() %></td><td><%=m.getClientName() %></td>
		<td><%=m.getContactAttempts() %></td><td><%=m.getDaysSinceLastContact()>-1 ? (""+m.getDaysSinceLastContact()) : "" %></td>
		<td><%=m.getPercentageMatch() %></td><td><%=m.getFormDataID() %></td>
		<td>
		<a href="?action=recordContactAttempt&clientID=<%=m.getClientID() %>&vacancyID=<%=vacancyID%>">Record attempt</a> | 
		<a href="?action=recordContact&clientID=<%=m.getClientID() %>&vacancyID=<%=vacancyID%>">Record contact</a>
		</td>
		</tr>
	<%}%>
	</table>
<%}%>
