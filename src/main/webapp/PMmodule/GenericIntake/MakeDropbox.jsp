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
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*,com.Ostermiller.util.NameValuePair"%>

<%
WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
String s_choice = request.getParameter("s_choice");
String s_label = request.getParameter("s_label");
String s_value = request.getParameter("s_value");
String submit_type = request.getParameter("submit_type");
IntakeNodeTemplate intakeNodeTemplate = (IntakeNodeTemplate) session.getAttribute("intakeNodeTemplate_c");
Integer lastTemplateId = (Integer) session.getAttribute("lastTemplateId");
Integer lastElementId = (Integer) session.getAttribute("lastElementId");
ArrayList<String> items = new ArrayList();


/* use a common list? */
List<NameValuePair> lists = new ArrayList<NameValuePair>();
IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
this.fillCommonLists(itn,lists); 

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
    intakeAnswerElement.setElement(s_value);
    if(s_label != null && s_label.length()>0) {
    	intakeAnswerElement.setLabel(s_label);
    }
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
} else if(submit_type.equals("copy_common_list")) {
	copyCommonElements(itn,Integer.valueOf(request.getParameter("select_common_list")),lastElementId,session,intakeNodeTemplate);
	Set intakeAnswerElements = intakeNodeTemplate.getAnswerElements();
    insertItems(items, intakeAnswerElements);
}
session.setAttribute("intakeNodeTemplate_c", intakeNodeTemplate);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Make Dropbox Scalar Choice</title>
<script type="text/javascript">
	    function copy_s(idx) {
		val="";
		if (idx>0) {
		    val=document.makeDropboxFrm.s_choice[idx].text;
		}
		document.makeDropboxFrm.s_value.value=val;
	    }
	    
	    function do_submit(s_type) {
		document.makeDropboxFrm.submit_type.value=s_type;
		document.makeDropboxFrm.submit();
	    }

	    function copy_common_list() {
			var sel = document.getElementById("select_common_list")
			var val = sel.options[sel.selectedIndex].value;
			if(val.length == 0) {return;}

			document.makeDropboxFrm.submit_type.value='copy_common_list';
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
	<% for (int i=0; i<items.size(); i++) { %>
		<option value="<%=i%>"><%=items.get(i)%></option>
	<% } %>
</select>
<p>&nbsp;</p>
Label: <input name="s_label" type="text" size="20" /> <br/>
Value: <input name="s_value" type="text" size="20" /> <br/>

<input type="button" value="+" title="Add new item" onclick="do_submit('add');" /> 
<input type="button" value="-" title="Remove selected item" onclick="do_submit('remove');" />

<p>&nbsp;</p>

-or-
<br/>
Copy Common List:
<br/>
<select id="select_common_list" name="select_common_list" onchange="copy_common_list()">
	<option value=""></option>
	<%for(NameValuePair nvp:lists) { %>
		<option value="<%=nvp.getValue()%>"><%=nvp.getName() %></option>
	<% } %>
</select>
<br/><br/>
<input type="button" value="Done" title="Save dropbox" onclick="do_submit('save');" /> 
<input type="hidden" name="submit_type" /></form>
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

void fillCommonLists(IntakeNode org,List<NameValuePair> lists) {
    if(org.getCommonList()) {
    	NameValuePair nvp = new NameValuePair(org.getLabelStr(),org.getIdStr());
    	lists.add(nvp);
    	
    }
    
    ArrayList children = new ArrayList();
    for (IntakeNode iN : org.getChildren()) {	
	fillCommonLists(iN,lists);
    }
   
}

void copyCommonElements(IntakeNode org, Integer nodeId,Integer lastElementId,HttpSession session,IntakeNodeTemplate intakeNodeTemplate) {
	if(org.getId().equals(nodeId)) {
		//found it
		List<IntakeAnswerElement> iaeList = new ArrayList<IntakeAnswerElement>();
		for(IntakeAnswerElement iae:org.getNodeTemplate().getAnswerElements()) {
			iaeList.add(iae);	
		}
				
		ListIterator<IntakeAnswerElement> listIterator = iaeList.listIterator(iaeList.size());
		//listIterator.
		
		while(listIterator.hasPrevious()) {
			
			IntakeAnswerElement iae = listIterator.previous();
			//make a copy, and add to current.
			   IntakeAnswerElement intakeAnswerElement = new IntakeAnswerElement();
			   intakeAnswerElement.setElement(iae.getElement());
			   intakeAnswerElement.setLabel(iae.getLabel());
			   lastElementId = (lastElementId==null) ? -1 : --lastElementId;
			   intakeAnswerElement.setId(lastElementId);
			   intakeAnswerElement.setNodeTemplate(intakeNodeTemplate);
			   
			   Set intakeAnswerElements = intakeNodeTemplate.getAnswerElements();
			   intakeAnswerElements.add(intakeAnswerElement);				   
		}
		
		session.setAttribute("lastElementId", lastElementId);		   		
	    
	    
	} else {
		ArrayList children = new ArrayList();
	    for (IntakeNode iN : org.getChildren()) {	
	    	copyCommonElements(iN,nodeId,lastElementId,session,intakeNodeTemplate);
	    }
	}
}
%>
