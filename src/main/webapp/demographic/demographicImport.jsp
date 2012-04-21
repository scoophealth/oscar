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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title>Import Demographic Information</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function displayAndDisable(){
    forms.Submit.disabled = true;
    showHideItem('waitingMessage');
    return true;
}
</SCRIPT>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">

<%
oscar.OscarProperties op = oscar.OscarProperties.getInstance();
String learningEnabled = op.getProperty("OSCAR_LEARNING");
if (!Util.checkDir(op.getProperty("TMP_DIR"))) { %>
<p>
<h2>Error! Cannot perform demographic import. Please contact support.</h2>

<%
} else {
%>

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Import <!--i18n--></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="import demographic" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn"><!--            
            <html:form action="/form/importUpload.do" method="POST" enctype="multipart/form-data" onsubmit="displayAndDisable()">
                        <input type="file" name="importFile" value="">                    
                        <input type="submit" name="Submit" value="Import">
            </html:form>
//--> <html:form action="/form/importUpload.do" method="POST"
			enctype="multipart/form-data" onsubmit="displayAndDisable()">
                        <p><html:file property="importFile" value=""/></p>
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
                        </html:radio><br>
                        <p><input type="submit" name="Submit" value="Import (CMS spec 4.0)"></p>
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
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
                
<% } %>
</body>
</html:html>
