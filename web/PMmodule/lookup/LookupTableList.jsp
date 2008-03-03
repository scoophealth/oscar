<%@ include file="/taglibs.jsp"%>
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="LookupTableList">Lookup Fields</th>
		</tr>
	</table>
</div>
<logic:iterate id="module" property="modules" name="lookupTableListForm" type="com.quatro.model.LookupCodeValue">
	<UL>
		<li>
			<c:out value="${module.description}"></c:out>
			<ul>
			<logic:iterate id="lkTable" property="associates" name="module" type="com.quatro.model.LookupCodeValue">
				<li><html:link action="/Lookup/LookupCodeList.do" paramName="lkTable" paramProperty="code" paramId="id">
				<bean:write name="lkTable" property="description"/></html:link> </li>
			</logic:iterate>
			</ul>
		</li>
	</UL>
</logic:iterate>
