<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page import="java.io.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%

	WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager"); 
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    String frmLabel = itn.getLabelStr();
    
    //response.flushBuffer();
    out.clear();
	out = pageContext.pushBody(); 

    response.setContentType ("binary/octet-stream");
    response.setHeader ("Content-Disposition", "attachment; filename=\"" + frmLabel + ".dat\"");

    ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
	oos.writeObject(itn);
  
    
%>
