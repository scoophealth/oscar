<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%@ include file="/taglibs.jsp"%>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    Integer frmVersion = (Integer) session.getAttribute("form_version");    
    String frmLabel = itn.getLabelStr();
    
    
    genericIntakeManager.deleteIntakeForm(itn);
    
    
    session.removeAttribute("intakeNode");
    session.removeAttribute("lastNodeId");
    session.removeAttribute("lastTemplateId");
    session.removeAttribute("lastElementId");
    session.removeAttribute("form_version");
    session.removeAttribute("latestFrmId");
    session.removeAttribute("form_version");
    
    response.sendRedirect("close_reset.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Delete Form</title>
</head>
<body>
<h2>Deleting [<%=frmLabel%> (<%=frmVersion%>)]...</h2>
</body>
</html>
