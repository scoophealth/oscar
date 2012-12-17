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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat,oscar.log.*"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.caisi.model.Tickler" %>
<%@ page import="org.caisi.dao.TicklerDAO" %>
<%
	TicklerDAO ticklerDao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
%>
<%
String[] param = new String[2];
String[] temp = request.getParameterValues("checkbox");
if (temp == null){
%>
<% response.sendRedirect("ticklerMain.jsp"); %>
<%}else{
		//temp=e.nextElement().toString();
    for (int i=0; i<temp.length; i++){
        param[0] = request.getParameter("submit_form").substring(0,1);
        param[1] = temp[i];

        Tickler t = ticklerDao.getTickler(Long.parseLong(temp[i]));
        if(t != null) {
        	t.setStatus(request.getParameter("submit_form").toCharArray()[0]);
        	ticklerDao.saveTickler(t);
        }

        String ip = request.getRemoteAddr();

        if (param[0] != null && param[0].equals("D") ){
			LogAction.addLog((String) session.getAttribute("user"), LogConst.DELETE, LogConst.CON_TICKLER, temp[i], ip);
        }else{
        	LogAction.addLog((String) session.getAttribute("user"), LogConst.UPDATE, LogConst.CON_TICKLER, temp[i], ip);
        }


    }
    response.sendRedirect("ticklerMain.jsp");
}
%>
