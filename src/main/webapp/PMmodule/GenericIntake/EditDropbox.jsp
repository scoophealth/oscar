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
    IntakeNode dbNode = (IntakeNode) session.getAttribute("dropboxNode");
    Integer lastTemplateId = (Integer) session.getAttribute("lastTemplateId");
    Integer lastElementId = (Integer) session.getAttribute("lastElementId");
    lastTemplateId = (lastTemplateId==null) ? -1 : --lastTemplateId;
    lastElementId = (lastElementId==null) ? -1 : --lastElementId;
    
    IntakeNodeTemplate iTemplate = null;
    if (dbNode==null) {
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
    String s_label = request.getParameter("s_label");
    String s_value = request.getParameter("s_value");
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
	    intakeAnswerElement.setLabel(s_label);
	    intakeAnswerElement.setElement(s_value);
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
			    lab=document.frm_editdb.box_item_2[idx].text;
			}
			document.frm_editdb.s_label.value=lab;
			document.frm_editdb.s_value.value=val;
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
		<td>
			<select name="box_item" size="9" onchange="copy_s(selectedIndex);">
				<% for (IntakeAnswerElement ie : items) { %>
					<option value="<%=ie.getId()%>"	<%=(ie.getId()==slct) ? "selected" : ""%>><%=ie.getElement()%></option>
				<% } %>
			</select>
			<div style="display:none;">
				<select name="box_item_2" size="9" >
					<% for (IntakeAnswerElement ie : items) { %>
						<option value="<%=ie.getId()%>"	<%=(ie.getId()==slct) ? "selected" : ""%>><%=ie.getLabel()%></option>
					<% } %>
				</select>
			</div>
		</td>
		<td>
			<input type="button" value="Move Up" onclick="move('up', box_item.value);move('up', box_item_2.value);" /> <br>
			<input type="button" value="Move Down" onclick="move('down', box_item.value);move('up', box_item_2.value);" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			Label: <input name="s_label" type="text" size="20" /><br/>
			Value: <input name="s_value" type="text" size="20" /> <br/>
			<input type="button" value="+" title="Add new item"	onclick="do_submit('add');" /> 
			<input type="button" value="-" title="Remove selected item" onclick="do_submit('remove');" /> <br>
			<input type="button" value="Done" title="Save dropbox" onclick="do_submit('save');" /> 
			<input type="hidden" name="submit_type" /> <input type="hidden" name="child_move" />
		</td>
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
