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

<%@ page import="java.util.*, oscar.*, oscar.util.*" errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>
<%
  // associate each operation with an output JSP file -- displaymode
  String[][] opToFile = new String[][] {
     {"Add Appointment" , "appointmentaddarecord.jsp"},
     {"Group Appt" , "appointmentgrouprecords.jsp"},
     {"Group Action" ,  "appointmentgrouprecords.jsp"},
     {"Add Appt & PrintPreview" , "appointmentaddrecordprint.jsp"},
     {"Add Appt & PrintCard" , "appointmentaddrecordcard.jsp"},
     {"PrintCard" , "appointmentviewrecordcard.jsp"},
     {"TicklerSearch" , "../tickler/ticklerAdd.jsp"},
     {"Search " , "../demographic/demographiccontrol.jsp"},
     {"Search" , "appointmentsearchrecords.jsp"},
     {"edit" , "editappointment.jsp"},
     {"Update Appt" , "appointmentupdatearecord.jsp"},
     {"Delete Appt" , "appointmentdeletearecord.jsp"},
     {"Cut" , "appointmentcutrecord.jsp"},
     {"Copy" , "appointmentcopyrecord.jsp"}
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
