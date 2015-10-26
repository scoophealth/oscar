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
<%@ include file="/taglibs.jsp" %>
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
    ArrayList nodes = new ArrayList();
    buildNodes(itn, nodes);
    
    String lblEdit = request.getParameter("lbledit");
    String questionIdEdit = request.getParameter("questionidedit");
    Integer id = Integer.parseInt(request.getParameter("id"));
    Integer nid = Integer.parseInt(request.getParameter("nid"));
    String mandatory = request.getParameter("mandatory");
    String repeating = request.getParameter("repeating");
    String cutPast = request.getParameter("cutpast");
    
    IntakeNode theNode = findNode(nid, nodes);
    boolean hasMandatory = (theNode.isQuestion() || theNode.isAnswerCompound());
    boolean hasRepeating = (theNode.isQuestion()|| theNode.isAnswerCompound()||theNode.isAnswerScalar());
    boolean hasCutPast = (!theNode.isIntake() && !theNode.isPage() && !theNode.isSection() && !theNode.isAnswerCompound() && theNode.getEq_to_id()!=null);
    boolean isDropbox = (theNode.isAnswerChoice() && !theNode.isAnswerBoolean());
    int typeId = theNode.getNodeTemplate().getType().getId();
  //  String validations = theNode.getValidations();
  	String[] validations = request.getParameterValues("validations");    
    
    if (isDropbox) {
	session.setAttribute("dropboxNode", theNode);
    }
    
    if (lblEdit != null){
        IntakeNodeLabel iLabel = new IntakeNodeLabel();
        iLabel.setId(id);
        iLabel.setLabel(lblEdit);
        
	writeLabel(nid, iLabel, nodes);
	if (hasMandatory) {
	    writeMandatory(mandatory, theNode);
	}
	
	writeRepeating(repeating,theNode);
	
	if (hasCutPast) {
	    writeCutPast(cutPast, theNode);
	}
	if(questionIdEdit != null) {
		writeQuestionId(questionIdEdit,theNode);
	}
	
    if(validations != null) {
    	String validationString="";
    	for(int x=0;x<validations.length;x++) {
    		if(x>0) {
    			validationString += ",";
    		}
    		validationString += validations[x];
    		validationString += ":true";    		
    	}
    	theNode.setValidations(validationString);
    }	
	
        response.sendRedirect("close.jsp");
        return;
    }
    
    IntakeNodeLabel eLabel = theNode.getLabel();
    String val = (eLabel!=null) ? eLabel.getLabel() : "";
    String questionidval=(theNode.getQuestionId()!=null)?theNode.getQuestionId():"";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Edit Node</title>
<script type="text/javascript">
	    function editDropbox() {
		var eURL = "EditDropbox.jsp";
		popup('300','400',eURL,'eddrpbx');
	    }
	</script>
<script language="javascript" type="text/javascript"
	src="<html:rewrite page="/share/javascript/Oscar.js"/>"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>

<h2>Edit Label</h2>
<form action="EditLabel.jsp" method="post">
<input type="text" name="lbledit" value="<%=val%>" />Label 
<br/>
<input type="text" name="questionidedit" value="<%=questionidval%>" />Internal Id (optional) 
<br/>
<input type="hidden" name="id"
	value="<%=id%>" /> <input type="hidden" name="nid" value="<%=nid%>" />

<%	if (hasMandatory) { %> 
	<br>
	<input type="checkbox" name="mandatory" value="true" <%=theNode.getMandatory() ? "checked" : ""%>>Mandatory</input> 
<%	} %> 


<% if(hasRepeating) {%>
	<br/>
	<input type="checkbox" name="repeating" value="true" <%=theNode.getRepeating() ? "checked" : ""%>>Repeating</input>	
<% } %>
<%	if (hasCutPast) { %>
<br>
<input type="checkbox" name="cutpast" value="true"
	<%=(theNode.getEq_to_id()==null || theNode.getEq_to_id()<0) ? "checked" : ""%>>Not
related to past forms</input> <%	} %> <%	if (isDropbox) { %> <br>
<input type="button" value="Edit Dropbox Items..." onclick="editDropbox();" /> 
<%	} %>
<br/>

<%if (typeId >= 4 && typeId <=9) {%>
<br/><br/>
Validations:
<br/>
<input type="checkbox" name="validations" value="required"/>Required<br/>
<input type="checkbox" name="validations" value="digits"/>Digits<br/>
<%}%>

<input type="submit" value="update" /> 
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

IntakeNode findNode(Integer nodeId, ArrayList nodes) {
    IntakeNode iNode = null;
    for (int i=0; i<nodes.size(); i++) {
	IntakeNode in = (IntakeNode) nodes.get(i);
	if (in.getId().equals(nodeId)) {
	    iNode = in;
	    i = nodes.size();
	}
    }
    return iNode;
}

void writeLabel(Integer nodeId, IntakeNodeLabel iLabel, ArrayList nodes) {
    IntakeNode iNode = findNode(nodeId, nodes);
    iNode.setLabel(iLabel);
}

void writeMandatory(String mand, IntakeNode iNode) {
    boolean mandatoryFlag = false;
    if (mand!=null) {
	mandatoryFlag = mand.equals("true") ? true : false;
    }
    iNode.setMandatory(mandatoryFlag);
}

void writeRepeating(String repeat, IntakeNode iNode) {
    boolean repeatingFlag = false;
    if (repeat!=null) {
    	repeatingFlag = repeat.equals("true") ? true : false;
    }
    iNode.setRepeating(repeatingFlag);
}

void writeCutPast(String cp, IntakeNode iNode) {
    int eqId = -iNode.getEq_to_id();
    if (cp!=null) {
	eqId = cp.equals("true") ? eqId : -eqId;
    }
    iNode.setEq_to_id(eqId);
}

void writeQuestionId(String questionId,IntakeNode iNode) {
	iNode.setQuestionId(questionId);
}
%>
