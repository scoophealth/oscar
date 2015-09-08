
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



<%@ include file="/casemgmt/taglibs.jsp"%>

<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%
String programId_str = (String)request.getSession().getAttribute("case_program_id");
Integer programId;
String programName=null;

if (programId_str == null || programId_str.length() == 0) {
	programId_str = "0";
} else {
	programId = Integer.valueOf(programId_str);

	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	programName = programDao.getProgramName(programId);
}

%>

<h3 style="color: red"><bean:message key="casemgmt.accessdenied" /></h3>
<p><bean:message key="casemgmt.clientNeverInProgram.error" /> <%=programName %>.</p>

<input type="button" value="Close Window" onclick="self.close()" />
