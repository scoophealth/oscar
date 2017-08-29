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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eform" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.sql.*, oscar.eform.data.*"%>
<%
	String id = request.getParameter("fid");
	String messageOnFailure = "No eform or appointment is available";
  if (id == null) {  // form exists in patient
      id = request.getParameter("fdid");
      String appointmentNo = request.getParameter("appointment");
      String eformLink = request.getParameter("eform_link");

      EForm eForm = new EForm(id);
      eForm.setContextPath(request.getContextPath());
      eForm.setOscarOPEN(request.getRequestURI());
      if ( appointmentNo != null ) eForm.setAppointmentNo(appointmentNo);
      if ( eformLink != null ) eForm.setEformLink(eformLink);

      String parentAjaxId = request.getParameter("parentAjaxId");
      if( parentAjaxId != null ) eForm.setAction(parentAjaxId);
      
      String html = eForm.getFormHtml();     
      if(oscar.eform.EFormUtil.shouldDisableUpdateForEForm(Integer.parseInt(eForm.getFid() )) ) {
    	  html = html.replaceAll("type=\"submit\"", "type=\"submit\" disabled=\"disabled\" title=\"Updates to existing eForm are disabled. Please create a new eform (here)\"");
      }
      out.print(html);
  } else {  //if form is viewed from admin screen
      EForm eForm = new EForm(id, "-1"); //form cannot be submitted, demographic_no "-1" indicate this specialty
      eForm.setContextPath(request.getContextPath());
      eForm.setupInputFields();
      eForm.setOscarOPEN(request.getRequestURI());
      eForm.setImagePath();
      
      String html = eForm.getFormHtml();     
      if(oscar.eform.EFormUtil.shouldDisableUpdateForEForm(Integer.parseInt(eForm.getFid() )) ) {
    	  html = html.replaceAll("type=\"submit\"", "type=\"submit\" disabled=\"disabled\" title=\"Updates to existing eForm are disabled. Please create a new eform (here)\"");
      }
      out.print(html);
  }
%>
<%
String iframeResize = (String) session.getAttribute("useIframeResizing");
if(iframeResize !=null && "true".equalsIgnoreCase(iframeResize)){ %>
<script src="<%=request.getContextPath() %>/library/pym.js"></script>
<script>
    var pymChild = new pym.Child({ polling: 500 });
</script>
<%}%>