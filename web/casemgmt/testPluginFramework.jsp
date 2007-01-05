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

<%org.springframework.web.context.WebApplicationContext ctx=
	org.springframework.web.context.support.WebApplicationContextUtils.
		getWebApplicationContext(config.getServletContext());

orgorg.oscarehr.PMmodule.service.ProgramManager programManager= 
	(org.oscarehr.PMmodule.service.ProgramManager)ctx.getBean("programManagerTest");
 

java.util.List list = programManager.getAllPrograms();%><%=list.size()%><%


new org.apache.struts.action.ActionForward("/PMmodule/Forms/SurveyExecute.do?method=survey&formId=aaa&clientId=1").toString();
%>ok<%
%>