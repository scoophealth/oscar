<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
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
			    copyIntakeNode(itn, nwItn,genericIntakeManager);
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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

void copyIntakeNode(IntakeNode org, IntakeNode cpy, GenericIntakeManager genericIntakeManager) {
    cpy.setLabel(org.getLabel());
    if(cpy.getLabel()!= null) {
    	cpy.getLabel().setId(null);
    }
    //need to check if template exists!
    IntakeNodeTemplate templ = genericIntakeManager.getIntakeNodeTemplate(org.getNodeTemplate().getId());
 	if(templ == null) {
 		org.getNodeTemplate().setId(null);
 		IntakeNodeType type = org.getNodeTemplate().getType(); 	
 		IntakeNodeTemplate tmplCpy = new IntakeNodeTemplate(null,new IntakeNodeType(type.getId(),type.getType()));
 		copyIntakeNodeTemplate(org.getNodeTemplate(),tmplCpy);
 		genericIntakeManager.saveIntakeNodeTemplate(tmplCpy);
 		cpy.setNodeTemplate(tmplCpy);
 	} else {
 		cpy.setNodeTemplate(org.getNodeTemplate());
 	}
 	
    cpy.setPos(org.getPos());
    cpy.setMandatory(org.getMandatory());
   	cpy.setValidations(org.getValidations());
   	
    if (!org.isIntake() && !org.isPage() && !org.isSection() && !org.isAnswerCompound()) {
        cpy.setEq_to_id(org.getEq_to_id());
    }
    
    ArrayList children = new ArrayList();
    for (IntakeNode iN : org.getChildren()) {
	IntakeNode child = new IntakeNode();
	child.setParent(org);
	copyIntakeNode(iN, child,genericIntakeManager);
	children.add(child);
    }
    if (!children.isEmpty()) cpy.setChildren(children);
}

void copyIntakeNodeTemplate(IntakeNodeTemplate org, IntakeNodeTemplate cpy) {
	//cpy.setAnswerElements()
	Set<IntakeAnswerElement> answers = new TreeSet<IntakeAnswerElement>();
	for(IntakeAnswerElement e:org.getAnswerElements()) {
		IntakeAnswerElement n = new IntakeAnswerElement();
		n.setDefault(e.isDefault());
		n.setElement(e.getElement());
		n.setLabel(e.getLabel());
		n.setValidation(e.getValidation());
		n.setNodeTemplate(cpy);	
		answers.add(n);
	}
	cpy.setAnswerElements(answers);
	
	if(org.getLabel() != null) {
		IntakeNodeLabel label=new IntakeNodeLabel();
		label.setLabel(org.getLabel().getLabel());
		cpy.setLabel(label);
	}
	
	cpy.setRemoteId(org.getRemoteId());

	if(org.getType() != null) {
		IntakeNodeType type = new IntakeNodeType(org.getType().getId(),org.getType().getType());
		type.setType(org.getType().getType());
		cpy.setType(type);
	}
	
}
%>