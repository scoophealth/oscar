<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    IntakeNode dbNode = (IntakeNode) session.getAttribute("dropboxNode");
    Integer lastTemplateId = (Integer) session.getAttribute("lastTemplateId");
    Integer lastElementId = (Integer) session.getAttribute("lastElementId");
    lastTemplateId = (lastTemplateId==null) ? -1 : --lastTemplateId;
    lastElementId = (lastElementId==null) ? -1 : --lastElementId;
    
    IntakeNodeTemplate iTemplate = null;
    if (dbNode==null) {
	System.out.println("IntakeNode cannot be NULL!!!");
	response.sendRedirect("close.jsp");
    } else {
	iTemplate = dbNode.getNodeTemplate();
    }
    
    String box_item = request.getParameter("box_item");
    Integer slct = -1;
    if (box_item!=null) {
	slct = Integer.parseInt(box_item);
    }
    String child_move = request.getParameter("child_move");
    String done_reorder = request.getParameter("done_reorder");
    String s_entry = request.getParameter("s_entry");
    String submit_type = request.getParameter("submit_type");
    ArrayList<IntakeAnswerElement> items = new ArrayList();

    if (iTemplate!=null) {
	lastElementId = prepareAnswerElements(iTemplate, lastElementId);
	session.setAttribute("lastElementId", lastElementId);
    }
	    
    if (submit_type!=null) {
	if (submit_type.equals("save")) {
	    response.sendRedirect("close.jsp");
	    return;

	} else if (submit_type.equals("add")) {
	    iTemplate = makeNewTemplate(dbNode, lastTemplateId);
	    IntakeAnswerElement intakeAnswerElement = new IntakeAnswerElement();
	    intakeAnswerElement.setElement(s_entry);
	    intakeAnswerElement.setId(lastElementId);
	    lastElementId--;
	    session.setAttribute("lastTemplateId", lastTemplateId);
	    session.setAttribute("lastElementId", lastElementId);

	    intakeAnswerElement.setNodeTemplate(iTemplate);
	    Set intakeAnswerElements = iTemplate.getAnswerElements();
	    intakeAnswerElements.add(intakeAnswerElement);
	    insertItems(items, intakeAnswerElements);

	} else if (submit_type.equals("remove")) {
	    iTemplate = makeNewTemplate(dbNode, lastTemplateId);
	    session.setAttribute("lastTemplateId", lastTemplateId);
	    Set intakeAnswerElements = iTemplate.getAnswerElements();
	    Object[] elementArray = intakeAnswerElements.toArray();
	    Integer r_choice = Integer.parseInt(box_item);
	    intakeAnswerElements.clear();
	    for (Object obj : elementArray) {
		IntakeAnswerElement iElement = (IntakeAnswerElement) obj;
		if (iElement.getId()!=r_choice) {
		    intakeAnswerElements.add(iElement);
		}
	    }
	    insertItems(items, intakeAnswerElements);

	} else if (submit_type.equals("reorder")) {
	    iTemplate = makeNewTemplate(dbNode, lastTemplateId);
	    session.setAttribute("lastTemplateId", lastTemplateId);
	    if (box_item!=null && child_move!=null) {
		slct = arrangeItems(box_item, child_move, iTemplate);
	    }
	}
	session.setAttribute("dropboxNode", dbNode);
    }
    items = getAnswerElement(iTemplate);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Dropbox Items</title>
<script type="text/javascript">
            function move(dirn, pos) {
		if (pos!="") {
		    frm_editdb.child_move.value=dirn;
		    do_submit('reorder');
		}
	    }
	    
	    function copy_s(idx) {
		val="";
		if (idx>=0) {
		    val=document.frm_editdb.box_item[idx].text;
		}
		document.frm_editdb.s_entry.value=val;
	    }
	    
	    function do_submit(s_type) {
		document.frm_editdb.submit_type.value=s_type;
		document.frm_editdb.submit();
	    }
	</script>
</head>
<body>
<h2>Edit Dropbox Items</h2>
<form name="frm_editdb" method="post" action="EditDropbox.jsp">

<table width="370">
	<tr>
		<td><select name="box_item" size="9"
			onchange="copy_s(selectedIndex);">
			<% for (IntakeAnswerElement ie : items) { %>
			<option value="<%=ie.getId()%>"
				<%=(ie.getId()==slct) ? "selected" : ""%>><%=ie.getElement()%></option>
			<% } %>
		</select></td>
		<td><input type="button" value="Move Up"
			onclick="move('up', box_item.value)" /> <br>
		<input type="button" value="Move Down"
			onclick="move('down', box_item.value)" /></td>
	</tr>
	<tr>
		<td colspan="2"><input name="s_entry" type="text" size="20" /> <input
			type="button" value="+" title="Add new item"
			onclick="do_submit('add');" /> <input type="button" value="-"
			title="Remove selected item" onclick="do_submit('remove');" /> <br>
		<input type="button" value="Done" title="Save dropbox"
			onclick="do_submit('save');" /> <input type="hidden"
			name="submit_type" /> <input type="hidden" name="child_move" /></td>
	</tr>
</table>
</form>
</body>
</html>

<%!

Integer arrangeItems(String pos_s, String mov, IntakeNodeTemplate iTemplate) {
    Integer pos = Integer.parseInt(pos_s);
    Integer idx = 0;
    Integer iid = 0;
    Set nwAnsElements = iTemplate.getAnswerElements();
    Object[] elementObjs = nwAnsElements.toArray();
    for (int i=0; i<elementObjs.length; i++) {
	IntakeAnswerElement iElement = (IntakeAnswerElement) elementObjs[i];
	if (iElement.getId().equals(pos)) {
	    idx = i;
	}
    }
    if (mov.equals("up") && idx>0) {
	IntakeAnswerElement ie1 = (IntakeAnswerElement) elementObjs[idx];
	IntakeAnswerElement ie2 = (IntakeAnswerElement) elementObjs[idx-1];
	iid = ie2.getId();
	ie2.setId(ie1.getId());
	ie1.setId(iid);
	
    } else if (mov.equals("down") && idx<elementObjs.length-1) {
	IntakeAnswerElement ie1 = (IntakeAnswerElement) elementObjs[idx];
	IntakeAnswerElement ie2 = (IntakeAnswerElement) elementObjs[idx+1];
	iid = ie2.getId();
	ie2.setId(ie1.getId());
	ie1.setId(iid);
    }
    
    nwAnsElements.clear();
    for (Object e : elementObjs) {
	nwAnsElements.add(e);
    }
    iTemplate.setAnswerElements(nwAnsElements);
    return iid;
}

Integer prepareAnswerElements(IntakeNodeTemplate iTemplate, Integer lastId) {
    Integer elementId = lastId;
    Object[] iElements = iTemplate.getAnswerElements().toArray();
    if (iElements.length>0) {
	IntakeAnswerElement ie = (IntakeAnswerElement) iElements[0];
	if (ie.getId()>0) {
	    for (int i=iElements.length; i>0; i--) {
		int j = iElements.length-i;
		ie = (IntakeAnswerElement) iElements[j];
		ie.setId(elementId-i);
	    }
	    elementId = lastId-iElements.length;
	}
    }
    return elementId;
}

ArrayList<IntakeAnswerElement> getAnswerElement(IntakeNodeTemplate iTemplate) {
    Object[] iElements = iTemplate.getAnswerElements().toArray();
    ArrayList<IntakeAnswerElement> eItems = new ArrayList();
    for (Object obj : iElements) {
	IntakeAnswerElement e = (IntakeAnswerElement) obj;
	eItems.add(e);
    }
    return eItems;
}

void insertItems(ArrayList<IntakeAnswerElement> itemList, Set elementSet) {
    Object[] elements = elementSet.toArray();
    for (int i=0; i<elements.length; i++) {
	IntakeAnswerElement e = (IntakeAnswerElement) elements[i];
	itemList.add(e);
    }
}

IntakeNodeTemplate makeNewTemplate(IntakeNode iNode, Integer lastId) {
    if (iNode.getNodeTemplate().getId()>0) {
	IntakeNodeTemplate intakeNodeTemplate = new IntakeNodeTemplate();
	IntakeNodeType intakeNodeType = new IntakeNodeType();
	intakeNodeType.setId(6);
	intakeNodeTemplate.setType(intakeNodeType);
	intakeNodeTemplate.setId(lastId);
	intakeNodeTemplate.setAnswerElements(iNode.getNodeTemplate().getAnswerElements());
	iNode.setNodeTemplate(intakeNodeTemplate);
	iNode.setNodeTemplate(intakeNodeTemplate);
	Set aeSet = iNode.getNodeTemplate().getAnswerElements();
	if (aeSet!=null) {
	    Object[] aeArray = aeSet.toArray();
	    for (Object obj : aeArray) {
		IntakeAnswerElement ie = (IntakeAnswerElement) obj;
		ie.setNodeTemplate(intakeNodeTemplate);
	    }
	}
    }
    return iNode.getNodeTemplate();
}
%>
