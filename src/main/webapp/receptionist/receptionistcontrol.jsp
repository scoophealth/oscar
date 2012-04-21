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

<%@ page import="java.util.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("receptionist"))
    response.sendRedirect("../logout.jsp");

  if(request.getParameter("year")==null && request.getParameter("month")==null && request.getParameter("day")==null && request.getParameter("displaymode")==null && request.getParameter("dboperation")==null) {
    GregorianCalendar now=new GregorianCalendar();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH)+1 ; //be care for the month +-1
    int nowDay = now.get(Calendar.DAY_OF_MONTH);
    response.sendRedirect("./receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday");
    return;
  }

  //associate each operation with an output JSP file -- displaymode
  String[][] opToFile = new String[][] {
    {"day" , "appointmentreceptionistadminday.jsp"},
    {"month" , "appointmentreceptionistadminmonth.jsp"},
    {"addstatus" , "receptionistaddstatus.jsp"},
    {"updatepreference" , "receptionistupdatepreference.jsp"},
    {"displaymygroup" , "receptionistdisplaymygroup.jsp"},
    {"newgroup" , "receptionistnewgroup.jsp"},
    {"savemygroup" , "receptionistsavemygroup.jsp"}
  };

  // create an operation-to-file dictionary
  UtilDict opToFileDict = new UtilDict();
  opToFileDict.setDef(opToFile);

  // create a request parameter name-to-value dictionary
  UtilDict requestParamDict = new UtilDict();
  requestParamDict.setDef(request);

  // get operation name from request
  String operation = requestParamDict.getDef("displaymode","");

  // redirect to a file associated with operation
  pageContext.forward(opToFileDict.getDef(operation,""));
%>
