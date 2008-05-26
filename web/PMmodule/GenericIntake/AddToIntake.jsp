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


if (request.getParameter("newpos") != null && request.getParameter("parent_intake_node_id") != null && request.getParameter("elementType") != null && request.getParameter("intake_node_label") != null){
    
    String npos                  = request.getParameter("newpos") ;
    String parent_intake_node_id = request.getParameter("parent_intake_node_id") ;
    String eleType               = request.getParameter("elementType") ;
    String intNodeLabel          = request.getParameter("intake_node_label") ;
    String mandatory		 = request.getParameter("mandatory") ;
    
    IntakeNodeLabel intakeNodeLabel = new IntakeNodeLabel();
    int lblId = -1;
    if (intNodeLabel != null){
	String lastLabel = (String) session.getAttribute("lastLabel");
	if (lastLabel!=null) {
	    lblId = Integer.parseInt(lastLabel)-1;
	}
	session.setAttribute("lastLabel", String.valueOf(lblId));
	intakeNodeLabel.setLabel(intNodeLabel);
	intakeNodeLabel.setId(lblId);
    }
    
    IntakeNode intakeNode = new IntakeNode();
    Integer lastNodeId = (Integer) session.getAttribute("lastNodeId");
    lastNodeId = (lastNodeId==null) ? -1 : --lastNodeId;
    intakeNode.setId(lastNodeId);
    session.setAttribute("lastNodeId", lastNodeId);
    
    IntakeNodeTemplate intakeNodeTemplate = null;
    if (eleType.equals("15")) {
	intakeNodeTemplate = (IntakeNodeTemplate) session.getAttribute("intakeNodeTemplate_c");
	session.removeAttribute("intakeNodeTemplate_c");
    } else {
	intakeNodeTemplate = (IntakeNodeTemplate) genericIntakeManager.getIntakeNodeTemplate(Integer.parseInt(eleType));
    }
    
    
    intakeNode.setNodeTemplate(intakeNodeTemplate);
    intakeNode.setLabel(intakeNodeLabel);
    intakeNode.setPos(Integer.parseInt(npos));
    if (mandatory!=null) {
	intakeNode.setMandatory(true);
    }
    
    IntakeNode parentNode = findNode(Integer.parseInt(parent_intake_node_id), nodes);
    intakeNode.setParent(parentNode);
    
    if (parentNode.getChildren()!=null) {
	List p_children = parentNode.getChildren();
	p_children.add(intakeNode);
    } else {
	ArrayList p_children = new ArrayList();
	p_children.add(intakeNode);
	parentNode.setChildren(p_children);
    }
    
    response.sendRedirect("close.jsp");
    return;
}


%>


<%
String id           = request.getParameter("id");
String nodeTemplate = request.getParameter("node");
String parentId     = request.getParameter("parentId");
String pos          = request.getParameter("pos");
String pSize        = request.getParameter("pSize");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add To Intake</title>
	<script type="text/javascript">
	    function type_selected(nTemplate, eType) {
		if (nTemplate=='4' || nTemplate=='5' || nTemplate=='6') {
		    for (var i=0; i<eType.length; i++) {
			if (eType[i].checked==true) {
			    return true;
			}
		    }
		    alert("Please select an element type!");
		    return false;
		}
		return true;
	    }
	    
	    function doMandatory() {
		if (document.addToIntakeFrm.mandatorySet.value==0 && document.forms[0].mandatory.checked==true) {
		    document.forms[0].mandatory.checked = false;
		}
	    }
	    
	    function mandSet(val) {
		document.addToIntakeFrm.mandatorySet.value=val;
		doMandatory();
	    }
	    
	    function makeDropbox() {
		var eURL = "MakeDropbox.jsp";
		popup('200','300',eURL,'mkdrpbx');
	    }
	</script>
        <script language="javascript" type="text/javascript" src="<html:rewrite page="/share/javascript/Oscar.js"/>" ></script>
    </head>
    <body>
        <form name="addToIntakeFrm" method="post" action="AddToIntake.jsp" onsubmit="return type_selected(<%=nodeTemplate%>, elementType);">
            
            <input type="hidden" name="newpos"                value="<%=pSize%>"/>
            
            <input type="hidden" name="parent_intake_node_id" value="<%=id%>" />
            
            
            <%if (nodeTemplate == null) {%>
            NO ELEMENT!
            <%}else if(nodeTemplate.equals("1") ){%>
            Add Section  
            <input type="hidden" name="elementType" value="4"/>
            <%}else if(nodeTemplate.equals("3") ){%>
            NADA
            <%}else if(nodeTemplate.equals("4") || nodeTemplate.equals("5")){%>
            +<input type="radio" name="elementType" value="5" onclick="mandSet(1);">question</input><br/>
            +<input type="radio" name="elementType" value="6" onclick="mandSet(1);"> answer compound</input> <br/>
            +<input type="radio" name="elementType" value="7" onclick="mandSet(0);"> answer scalar choice</input> <br/>
            +<input type="radio" name="elementType" value="15" onclick="mandSet(0); makeDropbox();"> answer scalar choice (dropbox)</input> <br/>
            +<input type="radio" name="elementType" value="8" onclick="mandSet(0);"> answer scalar text</input> <br/>
            +<input type="radio" name="elementType" value="13" onclick="mandSet(0);"> answer scalar note</input><br/>
            <%}else if(nodeTemplate.equals("6") ){%>
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> <br/>
            +<input type="radio" name="elementType" value="15" onclick="makeDropbox();"> answer scalar choice (dropbox)</input> <br/>
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> <br/>
            +<input type="radio" name="elementType" value="13"> answer scalar note</input><br/>
            <%}else{%>
            
            <%}%>
            
      
            <br> Label Text (Leave blank for no text):
            <input type="text" name="intake_node_label" />
	    <%if (nodeTemplate.equals("4") || nodeTemplate.equals("5")) {%>
	    <input type="checkbox" name="mandatory" onclick="doMandatory();">Mandatory</input>
	    <input type="hidden" name="mandatorySet"/>
            <%}%>
            <input type="submit" value="Add" />
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

%>
