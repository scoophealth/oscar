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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
  
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  String creatorDemo = request.getParameter("demo");
  
  if (creatorDemo == null){
      creatorDemo = request.getParameter("remarks");      
  }
  if (creatorDemo == null){
      creatorDemo = (String) request.getAttribute("demo");    
  }
  
  
%>

<%@page import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.common.dao.CtlRelationshipsDao" %>
<%@page import="org.oscarehr.common.model.CtlRelationships" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
	CtlRelationshipsDao ctlRelationshipsDao = SpringUtils.getBean(CtlRelationshipsDao.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="demographic.demographiceditdemographic.msgAddRelation" />  </title>
<!--I18n--> 
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />


<script type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle">
<!--  -->
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="demographic.demographiceditdemographic.msgAddRelation" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><oscar:nameage demographicNo="<%=creatorDemo%>" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="contact" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; 
		<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()){ %>
		
			<a href="<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=creatorDemo%>">Back to PMM </a>
		
		<%} %>
		
		</td>
		<td valign="top" class="MainTableRightColumn">

		<form id="ADDAPPT" method="post"
			action="../appointment/appointmentcontrol.jsp">
		<div>Name: <input type="text" name="keyword" size="25" value="" />

		<input type="submit" name="Submit" value="Search" /> <input
			type="hidden" name="orderby" value="last_name" /> 
                
<%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%>
                <input
			type="hidden" name="search_mode" value="<%=searchMode%>" /> <input
			type="hidden" name="originalpage"
			value="../demographic/AddAlternateContact.jsp" /> <input
			type="hidden" name="limit1" value="0" /> <input type="hidden"
			name="limit2" value="5" /> <input type="hidden" name="displaymode"
			value="Search " /> <input type="hidden" name="appointment_date"
			value="2002-10-01" /> <input type="hidden" name="status" value="t" />
		<input type="hidden" name="start_time" value="10:45"
			onchange="checkTimeTypeIn(this)" /> <input type="hidden" name="type"
			value="" /> <input type="hidden" name="duration" value="15" /> <input
			type="hidden" name="end_time" value="10:59"
			onchange="checkTimeTypeIn(this)" /> <input type="hidden"
			name="demographic_no" value="" /> <input type="hidden"
			name="location" tabindex="4" value="" /> <input type="hidden"
			name="resources" tabindex="5" value="" /> <input type="hidden"
			name="user_id" value="oscardoc, doctor" /> <input type="hidden"
			name="dboperation" value="search_demorecord" /> <input type="hidden"
			name="createdatetime" value="2002-10-1 17:53:50" /> <input
			type="hidden" name="provider_no" value="115" /> <input type="hidden"
			name="creator" value="oscardoc, doctor" /> <input type="hidden"
			name="remarks" value="<%=creatorDemo%>" /></div>
		</form>

		<%String demoNo = request.getParameter("demographic_no");               
                 String name = request.getParameter("name");
                 String origDemo = request.getParameter("remarks");
               if ( demoNo != null ) {%> <html:form
			action="/demographic/AddRelation">
			<input type="hidden" name="origDemo" value="<%=origDemo%>" />
			<input type="hidden" name="linkingDemo" value="<%=demoNo%>" />


			<div class="prevention">
			<fieldset><legend>Relation</legend> <label for="name">Name:<%=name%>
			<br />

			<label for="relation">Relationship:</label> <select name="relation">
			
			<%
				List<CtlRelationships> results = ctlRelationshipsDao.findAllActive();
				for(CtlRelationships t : results) {
					%>
						<option value="<%=t.getValue() %>"><%=t.getLabel() %></option>
					<%
				}
			%>
			<!-- 
				<option value="Mother">Mother</option>
				<option value="Father">Father</option>
				<option value="Father">Parent</option>
				<option value="Wife">Wife</option>
				<option value="Husband">Husband</option>
				<option value="Partner">Partner</option>
				<option value="Brother">Brother</option>
				<option value="Sister">Sister</option>
				<option value="Son">Son</option>
				<option value="Daughter">Daughter</option>
				<option value="Aunt">Aunt</option>
				<option value="Uncle">Uncle</option>
				<option value="GrandFather">GrandFather</option>
				<option value="GrandMother">GrandMother</option>
				<option value="Guardian">Guardian</option>
				<option value="Foster Parent">Foster Parent</option>
				<option value="Next of Kin">Next of Kin</option>
				<option value="Administrative Staff">Administrative Staff</option>
				<option value="Care Giver">Care Giver</option>
				<option value="Power of Attorney">Power of Attorney</option>
				<option value="Insurance">Insurance</option>
				<option value="Guarantor">Guarantor</option>
				<option value="Other">Other</option>
				-->
			</select> <input type="checkbox" name="sdm" value="yes"> Substitute
			Decision Maker</input> <input type="checkbox" name="emergContact" value="yes">
			Emergency Contact</input> <br />
			<label for="notes">Notes:</label><br>
			<textarea cols="20" rows="3" name="notes"></textarea> <input
				type="submit" value="Add Relationship" /></fieldset>
			</div>
		</html:form> <%}%>

		<div class="tablelisting">
		<table>
			<% DemographicRelationship demoRelation = new DemographicRelationship(); 
                  ArrayList list = demoRelation.getDemographicRelationships(creatorDemo);   
                  if (list.size() > 0){
               %>
			<tr>
				<th>Name</th>
				<th>Relation</th>
				<th>SDM</th>
				<th>Notes</th>
				<th>&nbsp;</th>
			</tr>

			<% }
                  for ( int i = 0; i < list.size(); i++ ){ 
                     HashMap h = (HashMap) list.get(i);
                     String relatedDemo = (String) h.get("demographic_no");              
                     DemographicData dd = new DemographicData();
                     org.oscarehr.common.model.Demographic demographic = dd.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), relatedDemo);    %>
			<tr>
				<td><%=demographic.getLastName() +", "+demographic.getFirstName()%></td>
				<td><%=h.get("relation")%></td>
				<td><%=returnYesIf1(h.get("sub_decision_maker"))%></td>
				<td><%=h.get("notes")%></td>
				<td><a
					href="DeleteRelation.do?id=<%=h.get("id")%>&amp;origDemo=<%=creatorDemo%>">del</a></td>
			</tr>
			<%}%>
		</table>
		</div>


		<oscar:oscarPropertiesCheck property="TORONTO_RFQ" value="yes">
			<br />
			<html:form action="/demographic/AddRelation">
				<input type="hidden" name="origDemo" value="<%=creatorDemo%>" />
				<input type="submit" name="pmmClient" value="Finished" />
			</html:form>
		</oscar:oscarPropertiesCheck></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
<%!
String returnYesIf1(Object o){
    String ret = "";
    if ( o instanceof String){
        String s = (String) o;
        if ( "1".equals(s)){
            ret = "yes";
        }
    }
    return ret;
}

%>
