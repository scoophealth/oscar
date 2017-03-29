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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.sql.*,oscar.oscarDB.*"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*"%>
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
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    String published = request.getParameter("published");
    String formType = request.getParameter("form_type");
    int frmType = 0;
    try {
	frmType = Integer.valueOf(formType).intValue();
    } catch(NumberFormatException e) {}
    IntakeNode itn = (IntakeNode) session.getAttribute("intakeNode");
    Integer frmVersion = (Integer) session.getAttribute("form_version");
    //String publisher = session.getAttribute("publisher").toString();
    String publisher = loggedInInfo.getLoggedInProvider().getFormattedName();
    String frmLabel = itn.getLabelStr();
    
    saveNodeLabels(itn, genericIntakeManager);
    saveNodeTemplate(itn, genericIntakeManager);
    saveAnswerElements(itn, genericIntakeManager);
    IntakeNode nwItn = new IntakeNode();
    copyIntakeNode(itn, nwItn);
    nwItn.setForm_version(frmVersion);
    nwItn.setFormType(Integer.valueOf(frmType).intValue());
    genericIntakeManager.saveIntakeNode(nwItn);
    updateEqId(nwItn, genericIntakeManager);

    if (published != null) {
    	updatePublish(nwItn, publisher, genericIntakeManager);        
    }
    
    if (published!=null && frmType == 1) {
	    genericIntakeManager.updateAgencyIntakeQuick(nwItn.getId());
    }
    session.removeAttribute("intakeNode");
    session.removeAttribute("lastNodeId");
    session.removeAttribute("lastTemplateId");
    session.removeAttribute("lastElementId");
    session.removeAttribute("form_version");
    
    session.setAttribute("latestFrmId", nwItn.getId());
    response.sendRedirect("close.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Save Form</title>
</head>
<body>
<h2>Saving [<%=frmLabel%>]...</h2>
</body>
</html>

<%!

void saveNodeLabels(IntakeNode in, GenericIntakeManager gim) {
    if (in.getLabel()!=null) {
	if (in.getLabel().getId()<0) {
	    in.getLabel().setId(null);
	}
	gim.saveNodeLabel(in.getLabel());
    }
    for (IntakeNode iN : in.getChildren()) {
	saveNodeLabels(iN, gim);
    }
}

void saveNodeTemplate(IntakeNode in, GenericIntakeManager gim) {
    if (in.getNodeTemplate()!=null) {
	IntakeNodeTemplate iTemplate = in.getNodeTemplate();
	if (iTemplate.getId()<0) {
	    iTemplate.setId(null);
	    Set iAnsElement = iTemplate.getAnswerElements();
	    if (iTemplate.getAnswerElements()!=null) {
		iTemplate.setAnswerElements(null);
	    }
	    gim.saveIntakeNodeTemplate(iTemplate);
	    iTemplate.setAnswerElements(iAnsElement);
	}
    }
    for (IntakeNode iN : in.getChildren()) {
	saveNodeTemplate(iN, gim);
    }
}

void saveAnswerElements(IntakeNode in, GenericIntakeManager gim) {
    if (in.getNodeTemplate()!=null) {
	if (in.getNodeTemplate().getAnswerElements()!=null) {
	    Object[] iElements = in.getNodeTemplate().getAnswerElements().toArray();
	    for (Object obj : iElements) {
		IntakeAnswerElement iElement = (IntakeAnswerElement) obj;
		if (iElement.getId()<0) {
		    iElement.setId(null);
		    gim.saveIntakeAnswerElement(iElement);
		}
	    }
	}
    }
    for (IntakeNode iN : in.getChildren()) {
	saveAnswerElements(iN, gim);
    }
}

void updateEqId(IntakeNode in, GenericIntakeManager gim) {
    if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	in.setEq_to_id(in.getId());
	gim.updateIntakeNode(in);
    }
    for (IntakeNode iN : in.getChildren()) {
	updateEqId(iN, gim);
    }
}

void updatePublish(IntakeNode in, String publisher, GenericIntakeManager gim) {
    in.setPublishDateCurrent();
    in.setPublish_by(publisher);
    gim.updateIntakeNode(in);
}

void copyIntakeNode(IntakeNode org, IntakeNode cpy) {
    cpy.setLabel(org.getLabel());
    cpy.setNodeTemplate(org.getNodeTemplate());
    cpy.setPos(org.getPos());
    cpy.setMandatory(org.getMandatory());
    cpy.setFormType(org.getFormType());
    cpy.setQuestionId(org.getQuestionId());
    cpy.setRepeating(org.getRepeating());
    cpy.setCommonList(org.getCommonList());
    cpy.setValidations(org.getValidations());
    if (!org.isIntake() && !org.isPage() && !org.isSection() && !org.isAnswerCompound()) {
        cpy.setEq_to_id(org.getEq_to_id());
    }
    
    ArrayList children = new ArrayList();
    for (IntakeNode iN : org.getChildren()) {
	IntakeNode child = new IntakeNode();
	child.setParent(org);
	copyIntakeNode(iN, child);
	children.add(child);
    }
    if (!children.isEmpty()) cpy.setChildren(children);
}

%>
