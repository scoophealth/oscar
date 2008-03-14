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
    
    prepareIntakeNode(itn);
    IntakeNode nwItn = new IntakeNode();
    copyIntakeNode(itn, nwItn);
    saveNodeLabels(itn, genericIntakeManager);
    genericIntakeManager.saveIntakeNode(nwItn);
    if (published!=null) {
        genericIntakeManager.updateAgencyIntakeQuick(nwItn.getId());
    }
    session.removeAttribute("intakeNode");
    response.sendRedirect("close.jsp?id=" + nwItn.getId());
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
	gim.saveNodeLabel(in.getLabel());
    }
    for (IntakeNode iN : in.getChildren()) {
	saveNodeLabels(iN, gim);
    }
}

void copyIntakeNode(IntakeNode org, IntakeNode cpy) {
    cpy.setAnswers(org.getAnswers());
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

void prepareIntakeNode(IntakeNode in) {
    if (in.getId()<0) {
	in.setId(null);
    }
    IntakeNodeLabel iLabel = in.getLabel();
    if (iLabel.getId()<0) {
	iLabel.setId(null);
    }
    for (IntakeNode iN : in.getChildren()) {
	prepareIntakeNode(iN);
    }
}

%>