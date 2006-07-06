<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.model.*" %>
<html>
<head>
	<title>Caisi Roles</title>

	<style type="text/css">
	/* <![CDATA[ */
	@import "<html:rewrite page="/css/core.css" />";
	/*  ]]> */
	</style>
	

	<script>
		function openBrWindow(theURL,winName,features) { 
		  window.open(theURL,winName,features);
		}
		
		function Check(e) {
			e.checked = true;
		}
		
		function Clear(e) {
			e.checked = false;
		}
		    
		function CheckAll() {
			var ml = document.ticklerForm;
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
					Check(e);
			    }
			}
		}
		
		function ClearAll() {
			var ml = document.ticklerForm;
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
				Clear(e);
			    }
			}
		}
	</script>	
</head>
<body>
<table border="0" cellspacing="0" cellpadding="1" width="100%" bgcolor="#CCCCFF">
	  <tr class="subject"><th colspan="4">Caisi Roles</th></tr>

<br/>
<%@ include file="messages.jsp" %>
<br/>

<form name="caisiRoleListForm" action="CaisiRoleAssigner.do" method="post">
<input type="hidden" name="change_provider" value="0"/>
<input type="hidden" name="method" value="assign"/>
<table width="50%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr class="title">
		<th>Provider Name</th>
		<th>Role</th>
	</tr>
	
	<tr>
		<%int index=0; 
			String bgcolor;
		%>
		<c:forEach var="provider" items="${providers}">
			<%
				if(index++%2!=0) {
					bgcolor="white";
				} else {
					bgcolor="#EEEEFF";
				}
			%>
			<tr bgcolor="<%=bgcolor %>" align="center">
				<td><c:out value="${provider.formattedName}"/></td>
				<td>
					<select name="select_<c:out value="${provider.provider_no}"/>" onchange="this.form.change_provider.value='<c:out value="${provider.provider_no}"/>';this.form.submit();">
						<option value="0"></option>
						<c:forEach var="role" items="${roles}">
							<%String selected="";
								Role role = (Role)pageContext.getAttribute("role");
								Provider p = (Provider)pageContext.getAttribute("provider");
								if(p != null && p.getRole() != null) {
									if((long)p.getRole().getRole_id() == role.getId().longValue()) {
										selected="selected";
									}
								}
							%>

								<option value="<c:out value="${role.id}"/>" <%=selected %> ><c:out value="${role.name}" /></option>

						</c:forEach>
					</select>
				</td>
			</tr>
		</c:forEach>
	</tr>
</table>
</form>
<br/>
<input type="button" value="Add new Role" onclick="location.href='CaisiRole.do'"/>
</body>
</html>