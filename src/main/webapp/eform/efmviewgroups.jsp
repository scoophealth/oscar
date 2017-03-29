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

<%@ page import="java.util.*, oscar.eform.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%   
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
   
   
String url = "";
if (request.getParameter("url") != null) 
    url = request.getParameter("url");
String groupView = "";
if (request.getParameter("group_view") != null) 
    groupView = request.getParameter("groupView");
String demographic_no = "";
if (request.getParameter("demographic_no") != null) 
    demographic_no = request.getParameter("demographic_no");
String patientGroups = "";
if (request.getParameter("patientGroups") != null) 
    patientGroups = request.getParameter("patientGroups");
String apptProvider = "";
if (request.getParameter("apptProvider") != null)
	apptProvider = request.getParameter("apptProvider");
String appointment = "";
if (request.getParameter("appointment") != null)
	appointment = request.getParameter("appointment");
    
String parentAjaxId = request.getParameter("parentAjaxId");

ArrayList groups;
if (patientGroups.equals("1")) {
    groups = EFormUtil.getEFormGroups(demographic_no);
} else {
    groups = EFormUtil.getEFormGroups();
}
%>
<form action="<%=url%>" name="groupselect" method="get">
	<input type="hidden" id="group_view" name="group_view" value="">
	<input type="hidden" name="demographic_no" value="<%=demographic_no%>">
	<input type="hidden" name="apptProvider" value="<%=apptProvider%>">
	<input type="hidden" name="appointment" value="<%=appointment%>">
	<input type="hidden" name="parentAjaxId" value="<%=parentAjaxId%>">
<div class="grouplist">
<div class="grouplistHeader"><bean:message key="eform.showmyform.msgViewGroup"/>:</div>
<ul class="grouplist">
	<li><a href="#" onclick="document.forms['groupselect'].submit()"><b><bean:message key="eform.showmyform.msgShowAll"/></b></a></li>
	<%for (int i=0; i<groups.size(); i++) {        
                String selected = "";
                HashMap curhash = (HashMap) groups.get(i);
                String group = (String) curhash.get("groupName");
                String size = (String) curhash.get("count");
                if (group.equals(groupView)) selected = "selected";%>
	<li class="<%=selected%>"><a href="#"
		onclick="document.getElementById('group_view').value='<%=group%>'; document.forms['groupselect'].submit();"><%=group%>
	(<%=size%>)</a></li>
	<% } %>
</ul>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>">
	<a href="#"
		onclick="popup(600, 1200, '../administration/?show=Forms&load=Groups', 'editGroups')"
		style="color: #835921;"><bean:message key="eform.showmyform.msgEditGroups"/></a>
</security:oscarSec></div>
</form>
