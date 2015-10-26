<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
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
