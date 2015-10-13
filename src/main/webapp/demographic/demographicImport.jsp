<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarDemographic.pageUtil.Util"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao, org.oscarehr.util.SpringUtils,org.oscarehr.PMmodule.model.Program"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	List<Program> programs = programDao.getAllPrograms();
	List<Program> courses = new ArrayList<Program>();
	for(Program p:programs) {
		if(p.getSiteSpecificField()!=null && p.getSiteSpecificField().equals("course")) {
			courses.add(p);
		}
	}

%>
<html:html locale="true">

<head>
<!--I18n-->
<title><bean:message key="admin.admin.DemoImport"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">


<SCRIPT LANGUAGE="JavaScript">
function displayAndDisable(){
    forms.Submit.disabled = true;
    showHideItem('waitingMessage');
    return true;
}
</SCRIPT>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>


<script>
$(document).ready(function(){
	$("#uploadWarn").tooltip();
});


</script>

</head>

<body vlink="#0000FF">

<%
oscar.OscarProperties op = oscar.OscarProperties.getInstance();
String learningEnabled = op.getProperty("OSCAR_LEARNING");
if (!Util.checkDir(op.getProperty("TMP_DIR"))) { %>
<p>
<h2>Error! Cannot perform demographic import. Please contact support.</h2>

<%
} else {
%>

<div class="container-fluid well">
	<h3><bean:message key="admin.admin.DemoImport"/></h3>
		
		<html:form action="/form/importUpload.do" method="POST"
			enctype="multipart/form-data" onsubmit="displayAndDisable()">
                        <p><html:file property="importFile" value=""/>
                        <span id="uploadWarn" title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
                        </p>
						<%if(learningEnabled != null && learningEnabled.equalsIgnoreCase("yes")) { %>
							<!-- Drop Down box of courses -->
							Course:&nbsp;<html:select property="courseId">
								<option value="0">Choose One</option>
								<%for(Program course:courses) { %>
									<option value="<%=course.getId().intValue()%>"><%=course.getName()%></option>
								<% } %>
							</html:select><br/>
							Timeshift (in days +/-):&nbsp;<html:text property="timeshiftInDays" value="0" size="5"/></br/>
						<%} %> 				
                        If patient's providers do not have OHIP numbers:<br>
                        <html:radio property="matchProviderNames" value="true">
                            Match providers in database by first and last names (Recommended)
                        </html:radio><br>
                        <html:radio property="matchProviderNames" value="false">
                            Import as new - same provider may have multiple entries
                        </html:radio><br><br>
                        <p><input class="btn btn-primary" type="submit" name="Submit" value="Import (CMS spec 4.0)"></p>
		</html:form>

		<div id="waitingMessage" style="display: none;">
		<h2>This may take several minutes</h2>
		</div>

		<% ArrayList list = (ArrayList)  request.getAttribute("warnings");
	       String importLog = (String) request.getAttribute("importlog");
               if ( list != null ) {
	          if (list.size()>0) {
            %>
		<div style="font-weight:bold;">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Warnings
                </div>
		<ul>
			<% for (int i = 0; i < list.size(); i++){
			  String warn = (String) list.get(i); %>
			<li><%=warn%></li>
			<%}%>
		</ul>
		<%}%><html:form action="/form/importLogDownload.do" method="POST">
			<input type="hidden" name="importlog" value="<%=importLog%>">
			<input type="submit" name="Submit" value="Download Import Event Log">
                     </html:form>
		<% } %>
</div>
                
<% } %>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

</body>
</html:html>
