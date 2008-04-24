<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%@ include file="/taglibs.jsp" %>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    String published = request.getParameter("published");
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    
    String frmLabel = itn.getLabelStr();
    
    saveNodeLabels(itn, genericIntakeManager);
    saveNodeTemplate(itn, genericIntakeManager);
    saveAnswerElements(itn, genericIntakeManager);
    IntakeNode nwItn = new IntakeNode();
    copyIntakeNode(itn, nwItn);
    genericIntakeManager.saveIntakeNode(nwItn);
    if (published!=null) {
        genericIntakeManager.updateAgencyIntakeQuick(nwItn.getId());
    }
    session.removeAttribute("intakeNode");
    session.removeAttribute("lastNodeId");
    session.removeAttribute("lastTemplateId");
    session.removeAttribute("lastElementId");
    response.sendRedirect("close.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Save Form</title>
    </head>
    <body>
	<h2>Saving [<%=frmLabel%>]...</h2>
    </body>
</html>

<%!

void saveNodeLabels(IntakeNode in, GenericIntakeManager gim) {
    if (in.getLabel()!=null) {
	if (in.getLabel().getId()<0) {
	    in.getLabel().setId(null);
	}
	gim.saveNodeLabel(in.getLabel());
    }
    for (IntakeNode iN : in.getChildren()) {
	saveNodeLabels(iN, gim);
    }
}

void saveNodeTemplate(IntakeNode in, GenericIntakeManager gim) {
    if (in.getNodeTemplate()!=null) {
	IntakeNodeTemplate iTemplate = in.getNodeTemplate();
	if (iTemplate.getId()<0) {
	    iTemplate.setId(null);
	    Set iAnsElement = iTemplate.getAnswerElements();
	    if (iTemplate.getAnswerElements()!=null) {
		iTemplate.setAnswerElements(null);
	    }
	    gim.saveIntakeNodeTemplate(iTemplate);
	    iTemplate.setAnswerElements(iAnsElement);
	}
    }
    for (IntakeNode iN : in.getChildren()) {
	saveNodeTemplate(iN, gim);
    }
}

void saveAnswerElements(IntakeNode in, GenericIntakeManager gim) {
    if (in.getNodeTemplate()!=null) {
	if (in.getNodeTemplate().getAnswerElements()!=null) {
	    Object[] iElements = in.getNodeTemplate().getAnswerElements().toArray();
	    for (Object obj : iElements) {
		IntakeAnswerElement iElement = (IntakeAnswerElement) obj;
		if (iElement.getId()<0) {
		    iElement.setId(null);
		    gim.saveIntakeAnswerElement(iElement);
		}
	    }
	}
    }
    for (IntakeNode iN : in.getChildren()) {
	saveAnswerElements(iN, gim);
    }
}

void copyIntakeNode(IntakeNode org, IntakeNode cpy) {
    cpy.setLabel(org.getLabel());
    cpy.setNodeTemplate(org.getNodeTemplate());
    cpy.setPos(org.getPos());
    cpy.setMandatory(org.getMandatory());
    
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