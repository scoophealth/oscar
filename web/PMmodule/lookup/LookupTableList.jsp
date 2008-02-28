<%@ include file="/taglibs.jsp"%>
 <table cellpadding="3" cellspacing="0" border="0" width="100%">
     <tr>
         <td style="color: white;font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px; font-weight: bold" background="../images/TitleBar2.png" align="center">
			Lookup Fields</td>
     </tr>
 </table>
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
