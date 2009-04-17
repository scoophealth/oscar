<%@ page import="java.util.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
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