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

<%@ page import="java.util.*, oscar.oscarReport.reportByTemplate.*"%>
     
<%
String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    		
ArrayList rawTemplates = (new ReportManager()).getReportTemplatesNoParam(roleName2$);
String templateViewId = request.getParameter("templateviewid");
if (templateViewId == null) templateViewId = "";

//go through the templates, and figure out all the categories
SortedSet<String> categories = new TreeSet<String>();
for(ReportObjectGeneric template:(ArrayList<ReportObjectGeneric>)rawTemplates) {
	if(template.getCategory() != null) {
		categories.add(template.getCategory());
	}
}
List<ReportObjectGeneric> templates = new ArrayList<ReportObjectGeneric>();

String selectedCategory = request.getParameter("category");
if(selectedCategory != null && selectedCategory.length()>0) {
	//filter the templates
	for(ReportObjectGeneric template:(ArrayList<ReportObjectGeneric>)rawTemplates) {
		if(template.getCategory() != null && template.getCategory().equals(selectedCategory)) {
			templates.add(template);
		}
	}
} else {
	templates = rawTemplates;
}

%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<div class="templatelist">
<a href="addEditTemplate.jsp" style="color: #226d55; font-size: 10px;">Add Template</a>
<br/>
<a href="javascript:void(0)" style="color: #226d55; font-size: 10px;" onclick="newWindow('<%=request.getContextPath()%>/oscarReport/reportByTemplate/k2aTemplates.jsp','templates')" title="<bean:message key='oscarReport.oscarReportByTemplate.msgK2ATemplate' />"><bean:message key="oscarReport.oscarReportByTemplate.msgDownloadFromK2A" /></a>
<div class="templatelistHeader">Filter by Category:</div>
<ul class="templatelist">
	<li>
		<form>
			<select name="category" onChange="this.form.submit()">
				<option value="" <%=(selectedCategory == null || selectedCategory.length() == 0)?" selected=\"selected\" ":"" %>>All</option>
				<%
				for(Iterator<String> i = categories.iterator(); i.hasNext();) {
					String x = i.next();
					%><option value="<%=x%>" <%=(selectedCategory != null && selectedCategory.equals(x))?" selected=\"selected\" ":"" %>><%=x %></option><%
				}
				%>
			</select>
		</form>
	</li>
</ul>

<div class="templatelistHeader">Select a template:</div>
<ul class="templatelist">
	<li><a href="homePage.jsp"><b>Main Page</b></a> <%
				class CustomComparator implements Comparator<ReportObject>{
			        public int compare(ReportObject r1, ReportObject r2){
			                return r1.getTitle().compareTo(r2.getTitle());
			        }
				}
				Collections.sort(templates,new CustomComparator());
				
				for (int i=0; i<templates.size(); i++) {
	                String selected = "";
	                ReportObject curReport = (ReportObject) templates.get(i);
	                String templateId = curReport.getTemplateId();
	                String templateTitle = curReport.getTitle();
	                String selectedTemplate = "";
	                if (templateId.equals(templateViewId)) selectedTemplate = "selectedTemplate";%>
	
	<li class="<%=selectedTemplate%>"><%=String.valueOf(i+1)%>. <a
		href="reportConfiguration.jsp?templateid=<%=templateId%>"><%=templateTitle%></a></li>
	<% } %>
</ul>
</div>
</form>
