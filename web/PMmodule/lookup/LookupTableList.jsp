<%@ include file="/taglibs.jsp"%>
<html>
<body>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="LookupTableList">Lookup Fields</th>
	</tr>
</table>
</div>
<logic:iterate id="module" property="modules" name="lookupTableListForm"
	type="com.quatro.model.LookupCodeValue">
	<UL>
		<li><c:out value="${module.description}"></c:out>
		<ul>
			<logic:iterate id="lkTable" property="associates" name="module"
				type="com.quatro.model.LookupCodeValue">
				<li><html:link action="/Lookup/LookupCodeList.do"
					paramName="lkTable" paramProperty="code" paramId="id">
					<bean:write name="lkTable" property="description" />
				</html:link></li>
			</logic:iterate>
		</ul>
		</li>
	</UL>
</logic:iterate>
<p></p>
<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="../admin/admin.jsp"> <img
			src="../images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"><bean:message key="global.btnBack" /></a></td>
		<!--  td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/><img src="../images/rightarrow.gif"  border="0" width="25" height="20" align="absmiddle"></a></td -->
	</tr>
</table>
</body>
</html>