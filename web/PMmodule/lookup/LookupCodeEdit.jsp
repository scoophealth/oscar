<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="../../taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/quatro-tag.tld" prefix="quatro"%>
<html>
<title>Lookup Code Edit</title>
<head>
<script type="text/javascript"
	src='<html:rewrite page="/js/quatroLookup.js" />'></script>
<script type="text/javascript">
	function submitForm()
	{
		document.forms[0].method.value="save";
		document.forms[0].submit();
	}
</script>
</head>
<body>
<html:form action="/Lookup/LookupCodeEdit">
	<html:hidden property="tableDef.tableId" />
	<html:hidden property="tableDef.moduleId" />
	<div style="color: red"><!--  %@ include file="messages.jsp" % -->
	<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="LookupTableList">Lookup Code Edit</th>
		</tr>
	</table>
	</div>
	<table cellpadding="3" cellspacing="0" border="0" width="100%"
		class="toolgroup">
		<tr>
			<td align="left"><input type="hidden" id="method" name="method"></input>
			<a href="javascript:submitForm();"> <img
				src="../images/Save16.png" border="0" /> Save </a> &nbsp;|&nbsp; <html:link
				action="/Lookup/LookupCodeList.do" paramId="id"
				paramName="lookupCodeEditForm" paramProperty="tableDef.tableId">
				<img src="../images/Back16.png" border="0" /> Back to Code List</html:link></td>
		</tr>
	</table>
	<logic:notEmpty name="lookupCodeEditForm" property="errMsg">
		<table width="100%">
			<tr>
				<td><b><bean:write name="lookupCodeEditForm"
					property="errMsg" /></b></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</logic:notEmpty>
	<table width="100%">
		<tr>
			<th width="20%">Category:</th>
			<th align="left"><bean:write name="lookupCodeEditForm"
				property="tableDef.moduleName" /></th>
		</tr>
		<tr>
			<th>Field:</th>
			<th align="left"><bean:write name="lookupCodeEditForm"
				property="tableDef.description" /></th>
		</tr>
		<tr>
			<logic:iterate id="field" name="lookupCodeEditForm"
				property="codeFields" indexId="fIndex"
				type="com.quatro.model.FieldDefValue">
				<tr>
					<td><bean:write name="field" property="fieldDesc" /></td>
					<td><logic:equal name="field" property="fieldType" value="S">
						<logic:empty name="field" property="lookupTable">
							<logic:equal name="field" property="editable" value="true">
								<html:text name="field" property="val" indexed="true"
									style="{width:100%}" />
							</logic:equal>
							<logic:equal name="field" property="editable" value="false">
								<bean:write name="field" property="val" />
								<html:hidden name="field" property="val" indexed="true"
									style="{width:100%}" />
							</logic:equal>
						</logic:empty>
						<logic:notEmpty name="field" property="lookupTable">
							<html:hidden name="field" property="lookupTable" indexed="true" />
							<quatro:lookupTag name="field"
								tableName="<%=field.getLookupTable()%>" indexed="true"
								formProperty="lookupCodeEditForm" codeWidth="10%"
								codeProperty="val" bodyProperty="valdesc"
								codeValue="<%=field.getVal()%>"
								bodyValue="<%=field.getValDesc()%>"></quatro:lookupTag>
						</logic:notEmpty>
					</logic:equal> <logic:equal name="field" property="fieldType" value="D">
						<bean:define id="dateVal" name="field" property="val"></bean:define>
						<quatro:datePickerTag name="field" property="val" indexed="true"
							openerForm="lookupCodeEditForm" width="200px"
							value="<%=oscar.MyDateFormat.getMyStandardDate((String)dateVal)%>"></quatro:datePickerTag>
					</logic:equal> <logic:equal name="field" property="fieldType" value="N">
						<logic:equal name="field" property="editable" value="true">
							<html:text name="field" property="val" indexed="true" />
						</logic:equal>
						<logic:equal name="field" property="editable" value="false">
							<bean:write name="field" property="val" />
							<html:hidden name="field" property="val" indexed="true" />
						</logic:equal>
					</logic:equal> <logic:equal name="field" property="fieldType" value="B">
						<html:select name="field" property="val" indexed="true">
							<html:option value="1">Yes</html:option>
							<html:option value="0">No</html:option>
						</html:select>
					</logic:equal></td>
				</tr>
			</logic:iterate>
	</table>
	</div>
</html:form>
</body>
</html>