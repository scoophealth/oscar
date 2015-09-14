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

<%@ page errorPage="../errorpage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="oscar.login.*"%>
<%@ page import="oscar.log.*"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.SecRole" %>
<%@ page import="org.oscarehr.common.dao.SecRoleDao" %>
<%@ page import="org.oscarehr.PMmodule.utility.RoleCache" %>

<%
	SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);

	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	String curUser_no = (String)session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  int ROLENAME_LENGTH = 30;
  String ip = request.getRemoteAddr();
  String msg = "Type in a role name and search it first to see if it is available.";
  String role_name = request.getParameter("role_name");
  String action = "search"; // add/edit
  Properties	prop  = new Properties();

  if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save")) {
    // check the input data

    if(request.getParameter("action").startsWith("edit")) {
      	// update the code
      	SecRole secRole = secRoleDao.findByName(request.getParameter("action").substring(4));
		if(secRole != null) {
			secRole.setName(role_name);
			secRoleDao.merge(secRole);
			RoleCache.reload();
			msg = role_name + " is updated.<br>" + "Type in a role name and search it first to see if it is available.";
  			action = "search";
		    prop.setProperty("role_name", role_name);
		    LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_ROLE, role_name, ip);
		} else {
			msg = role_name + " is <font color='red'>NOT</font> updated. Action failed! Try edit it again." ;
		    action = "edit" + role_name;
		    prop.setProperty("role_name", role_name);
		}

    } else if (request.getParameter("action").startsWith("add")) {
		if(role_name.equals(request.getParameter("action").substring("add".length()))) {
			SecRole secRole = new SecRole();
			secRole.setName(role_name);
			secRole.setDescription(role_name);
			secRoleDao.persist(secRole);
			RoleCache.reload();

  			msg = role_name + " is added.<br>" + "Type in a role name and search it first to see if it is available.";
  			action = "search";
		    prop.setProperty("role_name", role_name);
		    LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_ROLE, role_name, ip);
		} else {
      		msg = "You can <font color='red'>NOT</font> save the role  - " + role_name + ". Please search the role name first.";
  			action = "search";
		    prop.setProperty("role_name", role_name);
		}
    } else {
      msg = "You can <font color='red'>NOT</font> save the role. Please search the role name first.";
    }
  } else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Search")) {
    // check the input data
    if(role_name == null || role_name.length() < 2) {
      msg = "Please type in a role name.";
    } else {
    	SecRole secRole = null;
    	try {
    		secRole = secRoleDao.findByName(role_name);
    	}catch(javax.persistence.NoResultException e) {}

    	if(secRole != null) {
    		prop.setProperty("role_name", secRole.getName());
		    msg = "You can edit the role. (Please note: The change of the role may affect data in other tables.)";
		    action = "edit" + role_name;
    	} else {
    		prop.setProperty("role_name", role_name);
 		    msg = "It is a NEW role. You can add it.";
 		    action = "add" + role_name;
    	}
	}
  }
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Role</title>

<script type="text/javascript" language="JavaScript">
      <!--
		function setfocus() {
		  this.focus();
		  document.forms[0].role_name.focus();
		  document.forms[0].role_name.select();
		}
	    function onSearch() {
	        //document.forms[0].submit.value="Search";
	        var ret = checkreferral_no();
	        return ret;
	    }
	    function onSave() {
	        //document.forms[0].submit.value="Save";
	        var ret = checkreferral_no();
	        if(ret==true) {
				//ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        return ret;
	    }
		function checkreferral_no() {
	        var b = true;
	        if(document.forms[0].role_name.value.length<2){
	            b = false;
	            alert ("You must type in a role name.");
	        }
	        return b;
	    }
    function isreferral_no(s){
        // temp for 0.
    	if(s.length==0) return true;
    	if(s.length!=6) return false;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        return true;
    }
		function checkAllFields() {
	        var b = true;
	        if(document.forms[0].last_name.value.length<=0){
	            b = false;
	            alert ("The field \"Last Name\" is empty.");
	        } else if(document.forms[0].first_name.value.length<=0) {
	            b = false;
	            alert ("The field \"First Name\" is empty.");
	        }
			return b;
	    }
	    function isNumber(s){
	        var i;
	        for (i = 0; i < s.length; i++){
	            // Check that current character is number.
	            var c = s.charAt(i);
	            if (c == ".") continue;
	            if (((c < "0") || (c > "9"))) return false;
	        }
	        // All characters are numbers.
	        return true;
	    }
//-->

</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" style="margin: 0px">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td align="left">&nbsp;</td>
	</tr>
</table>

<center>
<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="80%">
	<tr BGCOLOR="#CCFFFF">
		<th><%=msg%></th>
	</tr>
</table>
</center>
<form method="post" name="baseurl" action="providerAddRole.jsp">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Role name</b></td>
		<td><input type="text" name="role_name"
			value="<%=prop.getProperty("role_name", "")%>" size='20'
			maxlength='30' /> <input type="submit" name="submit" value="Search"
			onclick="javascript:return onSearch();" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF" colspan="2"><input
			type="hidden" name="action" value='<%=action%>' /> <% if(!"search".equals(action)) {%>
		<input type="submit" name="submit"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
			onclick="javascript:return onSave();" /> <% }%> 
		</td>
	</tr>
</table>
</form>

<table>
	<tr>
		<td>Role Name:</td>
	</tr>
	<%
	List<SecRole> secRoles = secRoleDao.findAll();
	for(SecRole secRole:secRoles) {
		%>
		<tr>
			<td><%=secRole.getName()%></td>
		</tr>
	<%}%>

</table>
</body>
</html:html>
