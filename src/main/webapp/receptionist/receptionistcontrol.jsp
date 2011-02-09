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