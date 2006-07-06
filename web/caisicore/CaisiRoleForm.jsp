<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.model.*"  %>
<%@ page import="java.util.Calendar,java.util.GregorianCalendar" %>
	<%
	    GregorianCalendar now=new GregorianCalendar();
	    int curYear=now.get(Calendar.YEAR);
	    int curMonth=now.get(Calendar.MONTH);
	    int curDay=now.get(Calendar.DAY_OF_MONTH);
	%>
<html>
<head>
	<style type="text/css">
	/* <![CDATA[ */
	@import "<html:rewrite page="/css/core.css" />";
	/*  ]]> */
	</style>
	
	<script>
	function openBrWindow(theURL,winName,features) { 
	  window.open(theURL,winName,features);
	}
	
	function validateRoleForm(form) {
		if(form.elements['role.name'].value == '') {
			alert('You must provide a name');
			return false;
		}
	}
	</script>

	<title>Caisi Roles</title>
	
</head>
<body>
<div style="color:red">
<%@ include file="messages.jsp" %>
</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%" bgcolor="#CCCCFF">
	  <tr class="subject"><th colspan="4">Caisi Roles</th></tr>
	<tr>
            <td class="searchTitle" colspan="4">Existing Caisi Role List</td>
	</tr>
	
	<c:forEach var="role" items="${requestScope.roleList}">
	<tr>
            <td class="fieldValue" colspan="3"><c:out value="${role.name}" /></td>
	</tr>

	</c:forEach>
	<tr>
            <td class="searchTitle" colspan="4">Create New Caisi Role</td>
	</tr>
</table>

<br>

<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<html:form action="/CaisiRole" onsubmit="return validateRoleForm(this);">
	<input type="hidden" name="method" value="save">
              <tr>
                      <td class="fieldTitle">Name:</td>
                      <td class="fieldValue">
                      	<html:text property="role.name"/>
                      </td>
              </tr>
              <tr>
              	<td class="fieldValue" colspan="2" align="left">
				        <html:submit styleClass="button">Save</html:submit>				        
				        <input type="button" value="close" onclick="self.close();"/>
				</td>
	
				
              </tr>
		</html:form>
</table>         
</body>
</html>