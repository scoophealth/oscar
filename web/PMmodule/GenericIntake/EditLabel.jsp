<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%@ include file="/taglibs.jsp" %>
<%
  WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
  GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
  IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    ArrayList nodes = new ArrayList();
    buildNodes(itn, nodes);
    
    String lblEdit = request.getParameter("lbledit");
    String id = request.getParameter("id");
    String mandatory = request.getParameter("mandatory");
    
    IntakeNode theNode = findNode(Integer.parseInt(id), nodes);
    boolean hasMandatory = (theNode.isQuestion() || theNode.isAnswerCompound());
    
    if (lblEdit != null){
        IntakeNodeLabel iLabel = new IntakeNodeLabel();
        iLabel.setId(Integer.parseInt(id));
        iLabel.setLabel(lblEdit);
        
	writeLabel(iLabel, nodes);
	if (hasMandatory) {
	    writeMandatory(mandatory, theNode);
	}
	
        response.sendRedirect("close.jsp");
        return;
    }
    
    
    String val = "";
    if(id !=null){
        //IntakeNodeLabel eLabel = genericIntakeManager.getIntakeNodeLabel(Integer.parseInt(id));
	IntakeNodeLabel eLabel = findLabel(Integer.parseInt(id), nodes);
        val = eLabel!=null ? eLabel.getLabel() : "";
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Label</title>
        <style type="text/css">
        @import "<html:rewrite page="/css/genericIntake.css"/>";
        </style>
    </head>
    <body>

    <h2>Edit Label</h2>
    <form action="EditLabel.jsp" method="post">
        <input type="text" name="lbledit" value="<%=val%>"/>
        <input type="hidden" name="id" value="<%=id%>"/>
        
        <input type="submit" value="update"/>
     <%	if (hasMandatory) { %>
	<br><input type="checkbox" name="mandatory" value="true" <%=theNode.getMandatory() ? "checked" : ""%>>Mandatory</input>
     <%	} %>
    </form>
    
    </body>
</html>

<%!

void buildNodes(IntakeNode in, ArrayList aln) {
    aln.add(in);
    for (IntakeNode iN : in.getChildren()) {
	buildNodes(iN, aln);
    }
}

IntakeNode findNode(Integer labelId, ArrayList nodes) {
    IntakeNode iNode = null;
    for (int i=0; i<nodes.size(); i++) {
	IntakeNode in = (IntakeNode) nodes.get(i);
	if (in.getLabel()!=null) {
	    if (in.getLabel().getId().equals(labelId)) {
		iNode = in;
		i = nodes.size();
	    }
	}
    }
    return iNode;
}

IntakeNodeLabel findLabel(Integer labelId, ArrayList nodes) {
    IntakeNode iNode = findNode(labelId, nodes);
    return iNode.getLabel();
}

void writeLabel(IntakeNodeLabel iLabel, ArrayList nodes) {
    IntakeNode iNode = findNode(iLabel.getId(), nodes);
    iNode.setLabel(iLabel);
}

void writeMandatory(String mand, IntakeNode iNode) {
    boolean mandatoryFlag = false;
    if (mand!=null) {
	mandatoryFlag = mand.equals("true") ? true : false;
    }
    iNode.setMandatory(mandatoryFlag);
}

%>
