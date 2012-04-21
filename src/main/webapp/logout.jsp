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

<%@page import="java.util.HashMap, oscar.log.*"
	errorPage="errorpage.jsp"%>
<%
  if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled()) net.sf.cookierevolver.CRFactory.getManager().recordLogout(request);
  if(session != null) {
    Object user = session.getAttribute("user");
    if (user != null) {
      //HashMap hash=(HashMap)application.getAttribute("monitor");
      session.invalidate();
      request.getSession();
      String ip = request.getRemoteAddr();
	  LogAction.addLog((String)user, LogConst.LOGOUT, LogConst.CON_LOGIN, "", ip);
    }
  }
  String param = "";
  if(request.getParameter("login")!=null ) {
	  param = "?login="+request.getParameter("login") ;
  }
  response.sendRedirect("index.jsp"+param);
%>
