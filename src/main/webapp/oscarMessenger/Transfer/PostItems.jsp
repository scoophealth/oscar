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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page contentType='text/xml'
	import="oscar.oscarMessenger.docxfer.send.*, oscar.oscarMessenger.docxfer.util.*"%>
<%

    String checks = "";
    java.util.Enumeration names = request.getParameterNames();

    while(names.hasMoreElements())
    {
        String name = (String)names.nextElement();
        if(name.startsWith("item"))
        {
            if(request.getParameter(name).equalsIgnoreCase("on"))
            {
                checks += name.substring(4) + ",";
            }
        }
    }

    //out.println(checks);
    String xmlDoc = oscar.oscarMessenger.docxfer.util.MsgCommxml.decode64(request.getParameter("xmlDoc"));
    java.util.ArrayList aList = new java.util.ArrayList();
    String sXML = MsgCommxml.toXML(new MsgSendDocument().parseChecks2(xmlDoc, checks, aList));

    oscar.oscarMessenger.pageUtil.MsgSessionBean bean;
    bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
    bean.setAttachment(sXML);

    response.sendRedirect("../CreateMessage.jsp");


%>
