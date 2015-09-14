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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@ page import="java.util.*,java.sql.*,java.util.ResourceBundle"	errorPage="../provider/errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.MyGroupAccessRestrictionDao" %>
<%@ page import="org.oscarehr.common.model.MyGroupAccessRestriction" %>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();

	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	MyGroupAccessRestrictionDao myGroupAccessRestrictionDao = SpringUtils.getBean(MyGroupAccessRestrictionDao.class);

    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String curProvider_no = (String) session.getAttribute("user");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    List<String> groups = myGroupDao.getGroups();
    List<Provider> providers = providerDao.getActiveProviders();
    
    
    if(request.getParameter("method") != null && request.getParameter("method").equals("save")) {
    	String chosen_group = request.getParameter("chosen_group");
    	String providerNos[] = request.getParameterValues("data");
    	List<MyGroupAccessRestriction> fordel =  myGroupAccessRestrictionDao.findByGroupId(chosen_group);
    	for(MyGroupAccessRestriction m:fordel) {
    		myGroupAccessRestrictionDao.remove(m.getId());
    	}
    	if(providerNos != null) {    		    	
	    	for(String providerNoTemp : providerNos) {
	    		MyGroupAccessRestriction mgra = myGroupAccessRestrictionDao.findByGroupNoAndProvider(chosen_group,providerNoTemp);
	    		if(mgra != null) {
	    			myGroupAccessRestrictionDao.remove(mgra.getId());
	    		}
	    		
	   			mgra = new MyGroupAccessRestriction();
	   			mgra.setMyGroupNo(chosen_group);
	   			mgra.setProviderNo(providerNoTemp);
	   			mgra.setLastUpdateUser(providerNoTemp);
	       		myGroupAccessRestrictionDao.persist(mgra);
	    	}
    	}
    }
    
    List<MyGroupAccessRestriction> restrictions = new ArrayList<MyGroupAccessRestriction>();
    if(request.getParameter("chosen_group") != null && request.getParameter("chosen_group").length()>0) {
    	restrictions = myGroupAccessRestrictionDao.findByGroupId(request.getParameter("chosen_group"));    	
    }
%>

<html:html locale="true">
<head>
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
	<title><bean:message key="admin.groupacl.title" /></title>

	<script>
		function changeGroup() {
			$("#method").val('setGroupNo');
			$("#myform").submit();
		}
		
		function save_acl() {
			$("#method").val('save');
			$("#myform").submit();
		}
	</script>
</head>


<body topmargin="0" leftmargin="0" rightmargin="0">

<FORM id="myform" NAME="UPDATEPRE" METHOD="post" ACTION="groupnoacl.jsp">
	<input type="hidden" id="method" name="method"/>
	
	<table border=0 cellspacing=0 cellpadding=0 width="100%">
		<tr bgcolor="#486ebd">
			<th align=CENTER NOWRAP>
				<font face="Helvetica" color="#FFFFFF">
					<bean:message key="admin.groupacl.description" />
				</font>
			</th>
	</tr>
</table>

<center>

<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center">
					<font face="arial">
						Select Group:
					</font>
				</td>
				<td ALIGN="center">					
					<select id="chosen_group" name="chosen_group" onChange="changeGroup()">
						<option value=""></option>
						<%
							for(String group:groups) {
								String selected="";
								if(group.equals(request.getParameter("chosen_group"))) {
									selected=" selected=\"selected\" ";
								}
						%>
							<option value="<%=group%>" <%=selected%>><%=group %></option>
						<% } %>
						<%
							for(Provider provider:providers) {
								String selected="";
								if(provider.getProviderNo().equals(request.getParameter("chosen_group"))) {
									selected=" selected=\"selected\" ";
								}
						%>
							<option value="<%=provider.getProviderNo()%>" <%=selected%>><%=provider.getFormattedName() %></option>
						<% } %>
					</select>
				</td>
			</tr>
			<%			
				int i=0;
   				for (Provider provider:providers) {
   					String selected="";
   					
   					if(hasRestriction(restrictions,provider.getProviderNo())) {
   						selected=" checked=\"checked\" ";
   					}
   					   					
     				i++;
			%>
			<tr BGCOLOR="<%=i%2==0?"ivory":"white"%>">
				<td>&nbsp; <%=provider.getLastName()%>, <%=provider.getFirstName()%></td>
				<td ALIGN="center">
					<input type="checkbox" name="data" <%=selected%> value="<%=provider.getProviderNo()%>"> 					
			</tr>
			<% } %>			

		</table>

		</td>
	</tr>
</table>

</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center">
			<input type=button name="Submit" onClick="save_acl()" value="<bean:message key="admin.adminnewgroup.btnSubmit"/>"/>
			<INPUT TYPE="RESET" VALUE="<bean:message key="global.btnClose"/>" onClick="window.close();">
		</TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B></B></font></div>

</body>
</html:html>
<%!
	public boolean hasRestriction(List<MyGroupAccessRestriction> restrictions,String providerNo) {
		for(MyGroupAccessRestriction r:restrictions) {
			if(r.getProviderNo().equals(providerNo)) {
				return true;
			}
		}
		return false;
}
%>
