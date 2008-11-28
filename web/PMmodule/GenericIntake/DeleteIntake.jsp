<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    String id = request.getParameter("id");
    String pid = request.getParameter("parentId");
    
    ArrayList nodes = new ArrayList();
    buildNodes(itn, nodes);
    IntakeNode thisNode = findNode(Integer.parseInt(id), nodes);
    String thisLabel = thisNode.getLabelStr();
    removeNode(Integer.parseInt(id), Integer.parseInt(pid), nodes);
    
    response.sendRedirect("close.jsp");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Intake</title>
    </head>
    <body>
	<h2>Deleting [<%=thisLabel%>]...</h2>
    </body>
</html>

<%!

void removeNode(Integer id, Integer parent, ArrayList nodes) {
    if (id!=null & parent!=null) {
		IntakeNode pNode = findNode(parent, nodes);
		List children = pNode.getChildren();
		boolean found=false;
		for (int i=0; i<children.size(); i++) {
		    IntakeNode child = (IntakeNode) children.get(i);	 
		    if (child.getId().equals(id)) {
			children.remove(i);
			pNode.setChildren(children);
			found=true;
		    }
		}
		if(found) {
	    	//bug fix - positions need to be readjusted
	    	for (int i=0; i<children.size(); i++) {
	    		IntakeNode child = (IntakeNode) children.get(i);
	    		child.setPos(i);
	    	}
	    }
    }    
}

void buildNodes(IntakeNode in, ArrayList aln) {
    aln.add(in);
    for (IntakeNode iN : in.getChildren()) {
	buildNodes(iN, aln);
    }
}

IntakeNode findNode(Integer Id, ArrayList nodes) {
    IntakeNode iNode = null;
    for (int i=0; i<nodes.size(); i++) {
	IntakeNode in = (IntakeNode) nodes.get(i);
	if (in.getId()!=null) {
	    if (in.getId().equals(Id)) {
		iNode = in;
		i = nodes.size();
	    }
	}
    }
    return iNode;
}

%>

