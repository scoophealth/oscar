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

<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    
    String id = request.getParameter("id");
    String child_node = request.getParameter("child_node");
    String child_move = request.getParameter("child_move");
    String done_reorder = request.getParameter("done_reorder");
    Integer nwpos = -1;
    
    ArrayList nodes = new ArrayList();
    buildNodes(itn, nodes);
    IntakeNode thisNode = findNode(Integer.parseInt(id), nodes);
    
    if (child_move!=null && child_node!=null) {
	List nwChildNodes = thisNode.getChildren();
	nwpos = arrangeChild(thisNode, child_move, child_node, nwChildNodes);
	thisNode.setChildren(nwChildNodes);
    }
    
    IntakeNode[] children = new IntakeNode[thisNode.getChildren().size()];
    for (int i=0; i<children.length; i++) {
	children[i] = (IntakeNode) thisNode.getChildren().get(i);
    }
    
    if (done_reorder!=null) {
	if (done_reorder.equals("1")) {
	    response.sendRedirect("close.jsp");
	}
    }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Reorder Children</title>
<script type="text/javascript">
            function move(dirn, pos) {
		if (pos!="") {
		    form_reorder.child_move.value=dirn;
		    done("0");
		}
	    }
	    
	    function done(func) {
		form_reorder.done_reorder.value=func;
		form_reorder.submit();
	    }
	</script>
</head>
<body>
<h2>Reorder Children</h2>
<form name="form_reorder" method="post" action="ReorderNode.jsp">

<table width="370">
	<tr>
		<td><select name="child_node" size="9">
			<% for (int i=0; i<children.length; i++) { %>
			<option value="<%=children[i].getPos()%>"
				<%=children[i].getPos().equals(nwpos) ? "selected" : ""%>><%=children[i].getLabelStr()%></option>
			<% } %>
		</select></td>
		<td><input type="button" value="Move Up"
			onclick="move('up', child_node.value)" /> <br>
		<input type="button" value="Move Down"
			onclick="move('down', child_node.value)" /> <input type="hidden"
			name="done_reorder" /> <input type="hidden" name="child_move" /> <input
			type="hidden" name="id" value="<%=id%>" /></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="OK"
			onclick="done('1');" /></td>
	</tr>
</table>
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

Integer arrangeChild(IntakeNode node, String mov, String pos_s, List childList) {
    Integer pos = Integer.parseInt(pos_s);
    Integer idx = -1;
    for (int i=0; i<childList.size(); i++) {
	IntakeNode iN = (IntakeNode) childList.get(i);
	if (iN.getPos().equals(pos)) {
	    idx = i;
	}
    }
    
    IntakeNode n1=null, n2=null;
    Integer newPos = -1;
    if (mov.equals("up") && idx>0) {
	n1 = (IntakeNode) childList.get(idx);
	n2 = (IntakeNode) childList.get(idx-1);
	childList.set(idx, n2);
	childList.set(idx-1, n1);
    } else if (mov.equals("down") && idx<childList.size()-1) {
	n1 = (IntakeNode) childList.get(idx);
	n2 = (IntakeNode) childList.get(idx+1);
	childList.set(idx, n2);
	childList.set(idx+1, n1);
    }
    if (n1!=null && n2!=null) {
	Integer swap = n1.getPos();
	n1.setPos(n2.getPos());
	n2.setPos(swap);
	newPos = n1.getPos();
    }
    
    return newPos;
}
%>
