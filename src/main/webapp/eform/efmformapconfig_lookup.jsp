<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

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


<%@ page import="java.io.*, java.util.*, oscar.eform.*, oscar.eform.data.*, oscar.eform.EFormUtil"
%><input type="hidden" name="oscarAPCacheLookupType" value="<%= request.getParameter("oscarAPCacheLookupType") %>" /><%
String[] keys = request.getParameterValues("key");
if (keys == null) { keys = new String[0]; }
EFormLoader loader = EFormLoader.getInstance();
DatabaseAP ap;
String provider_no = (String) session.getValue("user");
String demographic_no = request.getParameter("demographic_no"); 
String fid = request.getParameter("fid");
EForm form = null;
form = new EForm("1", demographic_no);
form.setProviderNo(provider_no);  //needs provider for the action
form.setAppointmentNo(request.getParameter("appointment"));		
//form.setApptProvider(request.getParameter("apptProvider"));
for (String key : keys) {
	ap = EFormLoader.getAP(key);
	if (ap != null) {
		try {
			String sql = ap.getApSQL();
			String output = ap.getApOutput();
			//replace ${demographic} with demogrpahicNo
			if (sql != null) {
				sql = form.replaceAllFields(sql);
				
				ArrayList<String> names = DatabaseAP.parserGetNames(output); //a list of ${apName} --> apName
				sql = DatabaseAP.parserClean(sql);  //replaces all other ${apName} expressions with 'apName'
				ArrayList<String> values = EFormUtil.getValues(names, sql);
				if (values.size() != names.size()) {
					output = "";
				} else {
					for (int i=0; i<names.size(); i++) {
						output = DatabaseAP.parserReplace(names.get(i), org.apache.commons.lang.StringEscapeUtils.escapeHtml(values.get(i)), output);					}
				}
			}		
%><input type="hidden" name="<%=key%>" value="<%=output%>"/><%		
		}
		catch (Exception e) {			
%><input type="hidden" name="<%=key%>" value=""/><%			
		}
	}
	else {
%><input type="hidden" name="<%=key%>" value=""/><%	
	}
}
%>
