<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%@page import="org.apache.commons.fileupload.*"%>
<%@page import="java.io.*"%>

<%@ include file="/taglibs.jsp"%>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
 
    boolean isMultipart = FileUpload.isMultipartContent(request);
	if(isMultipart) {
	    FileUpload upload = new FileUpload(new DefaultFileItemFactory());
	    List<FileItem> items = upload.parseRequest(request);
	    for(FileItem item:items) {
	    	if(item.isFormField()) {
	    		String name = item.getFieldName();
	    	} else {
	    		ObjectInputStream ois = new ObjectInputStream(item.getInputStream());
				IntakeNode itn = (IntakeNode)ois.readObject();    					
				IntakeNode nwItn = new IntakeNode();
			    copyIntakeNode(itn, nwItn);
			    nwItn.setForm_version(1);
			    genericIntakeManager.saveIntakeNode(nwItn);
			    session.removeAttribute("intakeNode");
			    session.removeAttribute("lastNodeId");
			    session.removeAttribute("lastTemplateId");
			    session.removeAttribute("lastElementId");
			    session.removeAttribute("form_version");
			    session.setAttribute("latestFrmId", nwItn.getId());
			    response.sendRedirect("close.jsp");
	    	}
	    }	    	    
	}
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Import Form</title>
</head>
<body>
<h2>Importing</h2>
<form enctype="multipart/form-data" method="POST"><INPUT
	TYPE='file' NAME='formfile'> <INPUT TYPE='submit'
	VALUE='upload'></form>
</body>
</html>

<%!

void copyIntakeNode(IntakeNode org, IntakeNode cpy) {
    cpy.setLabel(org.getLabel());
    if(cpy.getLabel()!= null) {
    	cpy.getLabel().setId(null);
    }
    cpy.setNodeTemplate(org.getNodeTemplate());
    cpy.setPos(org.getPos());
    cpy.setMandatory(org.getMandatory());
    if (!org.isIntake() && !org.isPage() && !org.isSection() && !org.isAnswerCompound()) {
        cpy.setEq_to_id(org.getEq_to_id());
    }
    
    ArrayList children = new ArrayList();
    for (IntakeNode iN : org.getChildren()) {
	IntakeNode child = new IntakeNode();
	child.setParent(org);
	copyIntakeNode(iN, child);
	children.add(child);
    }
    if (!children.isEmpty()) cpy.setChildren(children);
}

%>