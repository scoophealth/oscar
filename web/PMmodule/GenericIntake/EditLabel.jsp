<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%@ include file="/taglibs.jsp"%>
<%
  WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
  GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
  IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    ArrayList nodes = new ArrayList();
    buildNodes(itn, nodes);
    
    String lblEdit = request.getParameter("lbledit");
    Integer id = Integer.parseInt(request.getParameter("id"));
    Integer nid = Integer.parseInt(request.getParameter("nid"));
    String mandatory = request.getParameter("mandatory");
    String cutPast = request.getParameter("cutpast");
    
    IntakeNode theNode = findNode(nid, nodes);
    boolean hasMandatory = (theNode.isQuestion() || theNode.isAnswerCompound());
    boolean hasCutPast = (!theNode.isIntake() && !theNode.isPage() && !theNode.isSection() && !theNode.isAnswerCompound() && theNode.getEq_to_id()!=null);
    boolean isDropbox = (theNode.isAnswerChoice() && !theNode.isAnswerBoolean());
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
	if (hasCutPast) {
	    writeCutPast(cutPast, theNode);
	}
	
        response.sendRedirect("close.jsp");
        return;
    }
    
    IntakeNodeLabel eLabel = theNode.getLabel();
    String val = (eLabel!=null) ? eLabel.getLabel() : "";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<form action="EditLabel.jsp" method="post"><input type="text"
	name="lbledit" value="<%=val%>" /> <input type="hidden" name="id"
	value="<%=id%>" /> <input type="hidden" name="nid" value="<%=nid%>" />

<input type="submit" value="update" /> <%	if (hasMandatory) { %> <br>
<input type="checkbox" name="mandatory" value="true"
	<%=theNode.getMandatory() ? "checked" : ""%>>Mandatory</input> <%	} %> <%	if (hasCutPast) { %>
<br>
<input type="checkbox" name="cutpast" value="true"
	<%=(theNode.getEq_to_id()==null || theNode.getEq_to_id()<0) ? "checked" : ""%>>Not
related to past forms</input> <%	} %> <%	if (isDropbox) { %> <br>
<input type="button" value="Edit Dropbox Items..."
	onclick="editDropbox();" /> <%	} %>
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

void writeCutPast(String cp, IntakeNode iNode) {
    int eqId = -iNode.getEq_to_id();
    if (cp!=null) {
	eqId = cp.equals("true") ? eqId : -eqId;
    }
    iNode.setEq_to_id(eqId);
}
%>
