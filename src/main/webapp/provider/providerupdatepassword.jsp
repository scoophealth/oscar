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

<%
	if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  MessageDigest md = MessageDigest.getInstance("SHA");
%>

<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.security.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Security" %>
<%@ page import="org.oscarehr.common.dao.SecurityDao" %>


<%
	SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
	List<Security> ss = securityDao.findByProviderNo(curUser_no);
	for(Security s:ss) {
		StringBuffer sbTemp = new StringBuffer();
	     byte[] btOldPasswd= md.digest(request.getParameter("oldpassword").getBytes());
	     for(int i=0; i<btOldPasswd.length; i++) sbTemp = sbTemp.append(btOldPasswd[i]);

	     String stroldpasswd = sbTemp.toString();

	     String strDBpasswd = s.getPassword();
	     if (strDBpasswd.length()<20) {
	         sbTemp = new StringBuffer();
	         byte[] btDBPasswd= md.digest(strDBpasswd.getBytes());
	         for(int i=0; i<btDBPasswd.length; i++) sbTemp = sbTemp.append(btDBPasswd[i]);
	         strDBpasswd = sbTemp.toString();
	     }
	     
	     if( stroldpasswd.equals(strDBpasswd ) && request.getParameter("mypassword").equals(request.getParameter("confirmpassword")) ) {
	       sbTemp = new StringBuffer();
	       byte[] btNewPasswd= md.digest(request.getParameter("mypassword").getBytes());
	       for(int i=0; i<btNewPasswd.length; i++) sbTemp = sbTemp.append(btNewPasswd[i]);

	       s.setPassword(sbTemp.toString());
	       securityDao.saveEntity(s);
	      
	       out.println("<script language='javascript'>self.close();</script>");
	     } else {
	       response.sendRedirect("providerchangepassword.jsp");
	     }
	}
%>
