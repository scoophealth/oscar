<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%@ include file="/taglibs.jsp"%>
<%
WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
String s_choice = request.getParameter("s_choice");
String s_entry = request.getParameter("s_entry");
String submit_type = request.getParameter("submit_type");
IntakeNodeTemplate intakeNodeTemplate = (IntakeNodeTemplate) session.getAttribute("intakeNodeTemplate_c");
Integer lastTemplateId = (Integer) session.getAttribute("lastTemplateId");
Integer lastElementId = (Integer) session.getAttribute("lastElementId");
ArrayList<String> items = new ArrayList();

if (submit_type==null) {
    intakeNodeTemplate = new IntakeNodeTemplate();
    IntakeNodeType intakeNodeType = new IntakeNodeType();
    intakeNodeType.setId(6);
    intakeNodeTemplate.setType(intakeNodeType);
    
    lastTemplateId = (lastTemplateId==null) ? -1 : --lastTemplateId;
    session.setAttribute("lastTemplateId", lastTemplateId);
    intakeNodeTemplate.setId(lastTemplateId);

} else if (submit_type.equals("save")) {
    response.sendRedirect("close.jsp");
    return;
    
} else if (submit_type.equals("add")) {
    IntakeAnswerElement intakeAnswerElement = new IntakeAnswerElement();
    intakeAnswerElement.setElement(s_entry);
    lastElementId = (lastElementId==null) ? -1 : --lastElementId;
    intakeAnswerElement.setId(lastElementId);
    session.setAttribute("lastElementId", lastElementId);
    
    intakeAnswerElement.setNodeTemplate(intakeNodeTemplate);
    Set intakeAnswerElements = intakeNodeTemplate.getAnswerElements();
    intakeAnswerElements.add(intakeAnswerElement);
    insertItems(items, intakeAnswerElements);
    
} else if (submit_type.equals("remove")) {
    Set intakeAnswerElements = intakeNodeTemplate.getAnswerElements();
    Object[] elementArray = intakeAnswerElements.toArray();
    Integer r_choice = Integer.parseInt(s_choice);
    intakeAnswerElements.clear();
    for (int i=0; i<elementArray.length; i++) {
	if (i!=r_choice) {
	    intakeAnswerElements.add(elementArray[i]);
	}
    }
    insertItems(items, intakeAnswerElements);
}
session.setAttribute("intakeNodeTemplate_c", intakeNodeTemplate);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Make Dropbox Scalar Choice</title>
<script type="text/javascript">
	    function copy_s(idx) {
		val="";
		if (idx>0) {
		    val=document.makeDropboxFrm.s_choice[idx].text;
		}
		document.makeDropboxFrm.s_entry.value=val;
	    }
	    
	    function do_submit(s_type) {
		document.makeDropboxFrm.submit_type.value=s_type;
		document.makeDropboxFrm.submit();
	    }
	</script>
<script language="javascript" type="text/javascript"
	src="<html:rewrite page="/share/javascript/Oscar.js"/>"></script>
</head>
<body>
<form name="makeDropboxFrm" method="post" action="MakeDropbox.jsp">
<select name="s_choice" onchange="copy_s(selectedIndex);">
	<option value="-1">- Type below to add new item -</option>
	<%
for (int i=0; i<items.size(); i++) {
%>
	<option value="<%=i%>"><%=items.get(i)%></option>
	<% } %>
</select>
<p>&nbsp;</p>
<input name="s_entry" type="text" size="20" /> <input type="button"
	value="+" title="Add new item" onclick="do_submit('add');" /> <input
	type="button" value="-" title="Remove selected item"
	onclick="do_submit('remove');" />
<p>&nbsp;</p>
<input type="button" value="Done" title="Save dropbox"
	onclick="do_submit('save');" /> <input type="hidden"
	name="submit_type" /></form>
</body>
</html>

<%!

void insertItems(ArrayList<String> itemList, Set elementSet) {
    Object[] elements = elementSet.toArray();
    for (int i=0; i<elements.length; i++) {
	IntakeAnswerElement e = (IntakeAnswerElement) elements[i];
	itemList.add(e.getElement());
    }
}

%>
