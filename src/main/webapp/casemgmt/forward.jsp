
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
<%@ page import="java.util.ArrayList"%>
<%@page import="oscar.util.SuperSiteUtil" %>

<!--  logic:redirect forward="/admissionListAction.admit"  / -->
<%
SuperSiteUtil.getInstance().checkSuperSiteAccess(request, response, "demographicNo");
%>
<%
    String useNewCaseMgmt;
    if((useNewCaseMgmt = request.getParameter("newCaseManagement")) != null ) {        
        session.setAttribute("newCaseManagement", useNewCaseMgmt); 
		ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
        if( users != null ) {
            users.add(request.getParameter("providerNo"));
            session.getServletContext().setAttribute("CaseMgmtUsers", users);
        }
    }
    else {
        useNewCaseMgmt = (String)session.getAttribute("newCaseManagement");                
   }
    
    if( useNewCaseMgmt != null && useNewCaseMgmt.equals("true") ) {    	
	%>
		<jsp:forward page="/CaseManagementEntry.do">
			<jsp:param name="method" value="setUpMainEncounter" />
			<jsp:param name="from" value="casemgmt" />
			<jsp:param name="chain" value="list" />
			<jsp:param name="demographicNo" value='<%=request.getParameter("demographicNo")%>' />
                        <jsp:param name="OscarMsgTypeLink" value='<%=request.getParameter("OscarMsgTypeLink")%>'/>
                        <jsp:param name="msgType" value='<%=request.getParameter("msgType")%>'/>
		</jsp:forward>
	<%
    }
    else {
	%>
		<jsp:forward page="/CaseManagementView.do" />
	<%
    }
%>
