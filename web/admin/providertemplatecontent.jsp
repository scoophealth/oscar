<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
        objectName="_admin,_admin.schedule" rights="r" reverse="<%=true%>">        
        <%response.sendRedirect("../logout.jsp");%>        
</security:oscarSec>
<%@ page import="java.util.*, java.sql.*, oscar.*,oscar.util.*"
        errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
        scope="page" />
<%@ include file="../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
{"search_template", "select * from encountertemplate where encountertemplate_name = ?" },
  };
  apptMainBean.doConfigure(dbParams,dbQueries);
%>

<%
  String tName = null;
  String tValue = null;
  ResultSet rsdemo = null;
    rsdemo = apptMainBean.queryResults(request.getParameter("name"), "search_template");
    while (rsdemo.next()) {
      tName = UtilMisc.charEscape(rsdemo.getString("encountertemplate_name"), '"');
      tValue = rsdemo.getString("encountertemplate_value");
        }
  apptMainBean.closePstmtConn();
%>
<%=tValue%>

