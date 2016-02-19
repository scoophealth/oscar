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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.eform.data.*"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
  if (request.getAttribute("page_errors") != null) {
%>
<script language=javascript type='text/javascript'>
function hideDiv() {
    if (document.getElementById) { // DOM3 = IE5, NS6
        document.getElementById('hideshow').style.display = 'none';
    }
    else {
        if (document.layers) { // Netscape 4
            document.hideshow.display = 'none';
        }
        else { // IE 4
            document.all.hideshow.style.display = 'none';
        }
    }
}
</script>
<div id="hideshow" style="position: relative; z-index: 999;"><a
	href="javascript:hideDiv()">Hide Errors</a> <font
	style="font-size: 10; font-color: darkred;"> <html:errors /> </font></div>
<% } %>

<%
	String provider_no = (String) session.getValue("user");
  String demographic_no = request.getParameter("demographic_no");
  String appointment_no = request.getParameter("appointment");
  String fid = request.getParameter("fid");
  String eform_link = request.getParameter("eform_link");
  String source = request.getParameter("source");
  

  EForm thisEForm = null;
  if (fid == null || demographic_no == null) {
      //if the info is in the request attribute
      thisEForm = (EForm) request.getAttribute("curform");
  } else {
      //if the info is in the request parameter
      thisEForm = new EForm(fid, demographic_no);
      thisEForm.setProviderNo(provider_no);  //needs provider for the action
  }

  if (appointment_no!=null) thisEForm.setAppointmentNo(appointment_no);
  if (eform_link!=null) thisEForm.setEformLink(eform_link);
  thisEForm.setContextPath(request.getContextPath());
  thisEForm.setupInputFields();
  thisEForm.setImagePath();
  thisEForm.setDatabaseAPs();
  thisEForm.setOscarOPEN(request.getRequestURI());
  thisEForm.setAction();
  thisEForm.setSource(source);
  out.print(thisEForm.getFormHtml());
%>
<%
String iframeResize = (String) session.getAttribute("useIframeResizing");
if(iframeResize !=null && "true".equalsIgnoreCase(iframeResize)){ %>
<script src="<%=request.getContextPath() %>/library/pym.js"></script>
<script>
    var pymChild = new pym.Child({ polling: 500 });
</script>
<%}%>