<%@ include file="/taglibs.jsp"%>


<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">Lookup Tables Management
			</span></th>
	</tr>
	<tr>
		<td align="left" class="buttonBar2"><html:link
			action="/PMmodule/Admin/SysAdmin.do"
			style="color:Navy;text-decoration:none;">
			<img border=0 src=<html:rewrite page="/images/close16.png"/> />&nbsp;Close&nbsp;&nbsp;|</html:link>
		</td>

	</tr>
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">




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


		</div>
		</td>
	</tr>
</table>